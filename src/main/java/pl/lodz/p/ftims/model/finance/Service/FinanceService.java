package pl.lodz.p.ftims.model.finance.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import pl.lodz.p.ftims.database.communicationinterface.IAccountDatabase;
import pl.lodz.p.ftims.database.managers.AccountDatabaseManager;
import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.finance.Model.Cashflow;
import pl.lodz.p.ftims.model.finance.Model.Transaction;
import pl.lodz.p.ftims.model.finance.Repository.CashflowRepository;
import pl.lodz.p.ftims.model.finance.Repository.TransactionRepository;
import pl.lodz.p.ftims.model.storage.model.Delivery;
import pl.lodz.p.ftims.model.storage.model.Order;
import pl.lodz.p.ftims.model.user.model.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class FinanceService implements IFinanceService {
    private IAccountDatabase accDatabase;
    private TransactionService transactionService;
    private CashflowService cashflowService;

    public FinanceService() {
        this.transactionService = new TransactionService(new TransactionRepository());
        this.cashflowService = new CashflowService(new CashflowRepository());
        this.accDatabase = new AccountDatabaseManager();
    }

    @Override
    public void addMoney(double amount) {
        accDatabase.addMoney(amount);
        Cashflow cf = new Cashflow(new GregorianCalendar(), amount, "Wpłata");
        addCashflow(cf);
    }

    @Override
    public void subtractMoney(double amount) {
        if (isEnough(amount)) {
            accDatabase.subMoney(amount);
            Cashflow cf = new Cashflow(new GregorianCalendar(), -amount, "Wypłata");
            addCashflow(cf);
        } else throw new IllegalArgumentException("Amount is too high");
    }

    @Override
    public double getBalance() {
        return accDatabase.getMoney();
    }

    @Override
    public boolean isEnough(double amount) {
        return (accDatabase.getMoney() >= amount);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return this.transactionService.getAllTransactions();
    }

    @Override
    public List<Transaction> getTransactionsBetweenDate(Calendar from, Calendar to) {
        return this.transactionService.getTransactionsBetweenDate(from, to);
    }

    @Override
    public List<Transaction> getMonthlyTransactions() {
        return this.transactionService.getMonthlyTransactions();
    }

    @Override
    public List<Transaction> getWeeklyTransactions() {
        return this.transactionService.getWeeklyTransactions();
    }

    @Override
    public List<Transaction> getDailyTransactions() {
        return this.transactionService.getDailyTransactions();
    }

    @Override
    public List<Transaction> getTransactionsBetweenPrice(double from, double to) {
        return this.transactionService.getTransactionsBetweenPrice(from, to);
    }

    @Override
    public List<Transaction> getClientTransactions(Client client) {
        return this.transactionService.getClientTransactions(client);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        double price = transaction.getPrice();
        if (transaction.isDelivery()) {
            // sprawdzenie czy produkty sa w magazynie
            boolean productsExist = true; // temp
            if (productsExist) {
                transaction.setState(true);
                this.transactionService.addTransaction(transaction);
                Cashflow cf = new Cashflow(new GregorianCalendar(), price, "Dostawa");
                addCashflow(cf);
            } else {
                transaction.setState(false);
                this.transactionService.addTransaction(transaction);
            }
        } else // zakup
        {
            if (isEnough(price)) {
                //usuniecie produktow z magazynu
                transaction.setState(true);
                this.transactionService.addTransaction(transaction);
                Cashflow cf = new Cashflow(new GregorianCalendar(), price, "Zakup klienta");
                addCashflow(cf);
            } else {
                transaction.setState(false);
                this.transactionService.addTransaction(transaction);
            }
        }
    }

    @Override
    public boolean paySalaries(List<User> employees) {
        double salaries = 0;

        for (User u : employees)
            salaries += u.getSalary();
        if (isEnough(salaries)) {
            accDatabase.subMoney(salaries);
            addCashflow(new Cashflow(new GregorianCalendar(), -salaries, "Wyplacenie pensji"));
            return true;
        }
        return false;
    }

    @Override
    public List<Cashflow> getAllCashflows() {
        return this.cashflowService.getAllCashflows();
    }

    @Override
    public List<Cashflow> getCashflowsBetweenDate(Calendar from, Calendar to) {
        return this.cashflowService.getCashflowsBetweenDate(from, to);
    }

    @Override
    public List<Cashflow> getMonthlyCashflows() {
        return this.cashflowService.getMonthlyCashflow();
    }

    @Override
    public List<Cashflow> getWeeklyCashflows() {
        return this.cashflowService.getWeeklyCashflow();
    }

    @Override
    public List<Cashflow> getDailyCashflows() {
        return this.cashflowService.getDailyCashflow();
    }

    @Override
    public void addCashflow(Cashflow cashflow) {
        this.cashflowService.addCashflow(cashflow);
    }

    @Override
    public Transaction getTransactionByDeliveryId(String id) {
        Transaction transaction = transactionService.getAllTransactions().stream().filter(item -> {
            Delivery transactionDelivery = item.getDelivery();
            if (transactionDelivery != null) {
                return transactionDelivery.getId().equals(id);
            }
            return false;
        }).findAny().orElse(null);
        return transaction;
    }

    @Override
    public Transaction getTransactionByOrderId(String id) {
        Transaction transaction = transactionService.getAllTransactions().stream().filter(item -> {
            Order transactionOrder = item.getOrder();
            if (transactionOrder != null) {
                return transactionOrder.getId().equals(id);
            }
            return false;
        }).findAny().orElse(null);
        return transaction;
    }

    @Override
    public void createTransactionRaport(List<Transaction> transactionList) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLineAtOffset(25, 725);
            contentStream.setLeading(14.5f);
            for (Transaction t : transactionList
            ) {
                contentStream.showText(t.toString());
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.close();
            StringBuilder sb = new StringBuilder();
            File file = new File("Raports");
            file.mkdir();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss");
            sb.append("./Raports/TransactionRaport_");
            sb.append(LocalDateTime.now().format(formatter));
            sb.append(".pdf");
            document.save(new File(sb.toString()));
            document.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
