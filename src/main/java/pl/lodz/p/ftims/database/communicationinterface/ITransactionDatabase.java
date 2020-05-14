package pl.lodz.p.ftims.database.communicationinterface;


import pl.lodz.p.ftims.model.finance.Model.Transaction;

import java.util.List;

public interface ITransactionDatabase {
    boolean addTransaction(Transaction transaction);

    boolean modifyTransaction(Transaction transaction);

    boolean removeTransaction(String id);

    List<Transaction> getTransactions();

    Transaction getTransaction(String id);
}
