package kdg.be.Managers.Interfaces;

import kdg.be.Modellen.Client;

import java.util.Optional;

public interface IClientManager {
    public Optional<Client> getClientById(long id);
    public Client makeOrUpdateClient(Client client);
    public Client removeClient(long id);
}
