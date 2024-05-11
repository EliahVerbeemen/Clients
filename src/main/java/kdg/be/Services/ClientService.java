package kdg.be.Services;

import kdg.be.Services.Interfaces.IClientManager;
import kdg.be.Modellen.Client;
import kdg.be.Modellen.Order;
import kdg.be.Repositories.ClientRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class ClientService implements IClientManager {
    ClientRepository _repo;
    public ClientService(ClientRepository repository){
        _repo = repository;
    }
    public Optional<Client> getClientById(long id){
        return _repo.findById(id);
    }
    @Override
    public Client makeClient(Client client) {
        return _repo.save(client);
    }

   // @Override
    public Client addOrderToClient(Client client, Order order) {
        Set<Order> orders=client.getOrders();
    //    Set<Order> ordersTwee=client.getClientOrders();
        orders.add(order);
     //   ordersTwee.add(order);


        return _repo.save(client);
    }


    @Override
    public void removeClient(long id) {

       _repo.deleteById(id);
    }



}
