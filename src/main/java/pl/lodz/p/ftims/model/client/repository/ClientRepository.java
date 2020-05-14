package pl.lodz.p.ftims.model.client.repository;

import pl.lodz.p.ftims.database.communicationinterface.IClientDatabase;
import pl.lodz.p.ftims.database.managers.ClientDatabaseManager;
import pl.lodz.p.ftims.model.client.model.Client;

import java.util.List;

public class ClientRepository implements IClientRepository {
    private IClientDatabase dataBaseService;

    public ClientRepository() {
        this.dataBaseService = new ClientDatabaseManager();
    }

    @Override
    public boolean addClient(Client client) { return dataBaseService.addClient(client); }

    @Override
    public boolean updateClient(Client client) { return dataBaseService.modifyClient(client); }

    @Override
    public boolean removeClient(String id) {
        return dataBaseService.removeClient(id);
    }

    @Override
    public Client getClient(String id) {
        return dataBaseService.getClient(id);
    }

    @Override
    public List<Client> getAll() {
        return dataBaseService.getClients();
    }
}
