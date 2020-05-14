package pl.lodz.p.ftims.model.finance.Service;

import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.finance.Model.Cashflow;
import pl.lodz.p.ftims.model.finance.Model.Transaction;
import pl.lodz.p.ftims.model.user.model.User;

import java.util.Calendar;
import java.util.List;

public interface IFinanceService {
    void addMoney(double amount);

    void subtractMoney(double amount);

    double getBalance();

    boolean isEnough(double amount);

    List<Transaction> getAllTransactions();

    List<Transaction> getTransactionsBetweenDate(Calendar from, Calendar to);

    List<Transaction> getMonthlyTransactions();

    List<Transaction> getWeeklyTransactions();

    List<Transaction> getDailyTransactions();

    List<Transaction> getTransactionsBetweenPrice(double from, double to);

    List<Transaction> getClientTransactions(Client client);

    void addTransaction(Transaction transaction);

    boolean paySalaries(List<User> employees);

    List<Cashflow> getAllCashflows();

    List<Cashflow> getCashflowsBetweenDate(Calendar from, Calendar to);

    List<Cashflow> getMonthlyCashflows();

    List<Cashflow> getWeeklyCashflows();

    List<Cashflow> getDailyCashflows();

    void addCashflow(Cashflow cashflow);

    Transaction getTransactionByDeliveryId(String id);

    Transaction getTransactionByOrderId(String id);

    void createTransactionRaport(List<Transaction> transactionList);
}
