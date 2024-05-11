package kdg.be.Services.Interfaces;

import kdg.be.Modellen.Client;

import java.util.Optional;

public interface IClientManager {
    public Optional<Client> getClientById(long id);
    public Client makeClient(Client client);
    public void removeClient(long id);
}
