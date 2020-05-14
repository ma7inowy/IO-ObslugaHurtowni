package pl.lodz.p.ftims.database.communicationinterface;

import pl.lodz.p.ftims.model.client.model.Client;

import java.util.List;

public interface IClientDatabase {

    boolean addClient(Client client);

    boolean modifyClient(Client client);

    boolean removeClient(String id);

    List<Client> getClients();

    Client getClient(String id);
}
