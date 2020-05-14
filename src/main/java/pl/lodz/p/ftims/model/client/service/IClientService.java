package pl.lodz.p.ftims.model.client.service;

import pl.lodz.p.ftims.model.client.model.Client;

import java.util.List;

public interface IClientService {

     void addClient(String name);

     List<Client> getClients() ;

     void removeClient(Client client) ;

     Client getClient(Client client);

     Client getClient(String clientId);

     void updateClient(Client client);

    List<String> getAllClientsName();

    Client getClientByName(String name);

}
