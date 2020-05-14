package pl.lodz.p.ftims.model.client.service;

import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.finance.Model.Transaction;

import java.util.List;

public interface IClientTransactionService {

    List<Transaction> getClientTransactions(Client client);
}
