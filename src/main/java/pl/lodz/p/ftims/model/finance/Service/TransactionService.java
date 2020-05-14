package pl.lodz.p.ftims.model.finance.Service;

import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.finance.Model.Transaction;
import pl.lodz.p.ftims.model.finance.Repository.TransactionRepository;

import java.util.Calendar;
import java.util.List;

public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean deleteTransaction(String id) {
        return transactionRepository.deleteTransaction(id);
    }

    public Transaction getTransaction(String id) {
        return this.transactionRepository.getTransaction(id);
    }

    public List<Transaction> getAllTransactions() {
        return this.transactionRepository.getAllTransactions();
    }

    public List<Transaction> getTransactionsBetweenDate(Calendar from, Calendar to) {
        return this.transactionRepository.getTransactionsBetweenDate(from, to);
    }

    public List<Transaction> getTransactionsBetweenPrice(double from, double to) {
        return this.transactionRepository.getTransactionsBetweenPrice(from, to);
    }

    public List<Transaction> getMonthlyTransactions() {
        return this.transactionRepository.getMonthlyTransactions();
    }

    public List<Transaction> getWeeklyTransactions() {
        return this.transactionRepository.getWeeklyTransactions();
    }

    public List<Transaction> getDailyTransactions() {
        return this.transactionRepository.getDailyTransactions();
    }

    public List<Transaction> getClientTransactions(Client client) {
        return this.transactionRepository.getClientTransactions(client);
    }

    public void addTransaction(Transaction transaction) {
        this.transactionRepository.addTransaction(transaction);
    }
}
