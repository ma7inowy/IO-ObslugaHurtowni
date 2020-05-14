package pl.lodz.p.ftims.model.client.repository;
import pl.lodz.p.ftims.model.client.model.Client;

import java.util.List;

public interface IClientRepository {
    boolean addClient(Client client);

    boolean updateClient(Client client);

    boolean removeClient(String id);

    Client getClient(String id);

    List<Client> getAll();
}
