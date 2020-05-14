package pl.lodz.p.ftims.model.client.service;

import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.client.repository.ClientRepository;
import pl.lodz.p.ftims.model.client.repository.IClientRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ClientService implements IClientService {
    private IClientRepository clientRepository;

    public ClientService() {
        this.clientRepository = new ClientRepository();
    }


    public void addClient(String name) {
        clientRepository.addClient(new Client(name));
    }

    public List<Client> getClients() {
        return clientRepository.getAll();
    }

    public void removeClient(Client client) {
        clientRepository.removeClient(client.getId());
    }

    public Client getClient(Client client){
        return clientRepository.getClient(client.getId());
    }

    public Client getClient(String clientId){
        return clientRepository.getClient(clientId);
    }

    public void updateClient(Client client){
         clientRepository.updateClient(client);
    }

    public List<String> getAllClientsName(){
        return clientRepository.getAll().stream().map(Client::getName).collect(Collectors.toList());
    }

    public Client getClientByName(String name){
        return clientRepository.getAll().stream().filter(client -> client.getName().equals(name)).findFirst().orElse(null);
    }

}

