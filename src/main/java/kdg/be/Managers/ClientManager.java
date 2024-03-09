package kdg.be.Managers;

import kdg.be.Managers.Interfaces.IClientManager;
import kdg.be.Modellen.Client;
import kdg.be.Repositories.ClientRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClientManager implements IClientManager {
    ClientRepository _repo;
    public ClientManager(ClientRepository repository){
        _repo = repository;
    }
    public Optional<Client> getClientById(long id){
        return _repo.findById(id);
    }
    @Override
    public Client makeOrUpdateClient(Client client) {
        return _repo.save(client);
    }

    @Override
    public Client removeClient(long id) {
        return _repo.deleteClientByClientId(id);
    }
}
