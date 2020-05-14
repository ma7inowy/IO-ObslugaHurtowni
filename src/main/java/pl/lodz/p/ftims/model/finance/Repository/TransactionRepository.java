package pl.lodz.p.ftims.model.finance.Repository;

import pl.lodz.p.ftims.database.communicationinterface.ITransactionDatabase;
import pl.lodz.p.ftims.database.managers.TransactionDatabaseManager;
import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.finance.Model.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransactionRepository {
    private ITransactionDatabase database;
    private List<Transaction> transactions;

    public TransactionRepository() {
        this.database = new TransactionDatabaseManager();
        this.transactions = new ArrayList<>();
    }

    public boolean deleteTransaction(String id) {
        return database.removeTransaction(id);
    }

    public Transaction getTransaction(String id) {
        return database.getTransaction(id);
    }

    public List<Transaction> getAllTransactions() {
        transactions = database.getTransactions();
        return transactions;
    }

    public List<Transaction> getTransactionsBetweenDate(Calendar from, Calendar to) {
        getAllTransactions();
        List<Transaction> tmp = new ArrayList<>();

        for (Transaction t : transactions)
            if (t.getDate().after(from) && t.getDate().before(to))
                tmp.add(t);

        return tmp;
    }

    public List<Transaction> getTransactionsBetweenPrice(double from, double to) {
        getAllTransactions();
        List<Transaction> tmp = new ArrayList<>();

        for (Transaction t : transactions)
            if (t.getPrice() >= from && t.getPrice() <= to)
                tmp.add(t);

        return tmp;
    }

    public List<Transaction> getMonthlyTransactions() {
        getAllTransactions();
        Calendar tmp = Calendar.getInstance();
        tmp.add(Calendar.MONTH, -1);
        return getTransactionsBetweenDate(tmp, Calendar.getInstance());
    }

    public List<Transaction> getWeeklyTransactions() {
        getAllTransactions();
        Calendar tmp = Calendar.getInstance();
        tmp.add(Calendar.DAY_OF_MONTH, -7);
        return getTransactionsBetweenDate(tmp, Calendar.getInstance());
    }

    public List<Transaction> getDailyTransactions() {
        getAllTransactions();
        Calendar tmp = Calendar.getInstance();
        tmp.add(Calendar.DAY_OF_MONTH, -1);
        return getTransactionsBetweenDate(tmp, Calendar.getInstance());
    }

    public List<Transaction> getClientTransactions(Client client) {
        getAllTransactions();
        List<Transaction> tmp = new ArrayList<>();

        for (Transaction t : transactions)
            if (t.getClient() != null && t.getClient().getId().equals(client.getId()))
                tmp.add(t);

        return tmp;
    }

    public void addTransaction(Transaction transaction) {
        database.addTransaction(transaction);
    }
}
