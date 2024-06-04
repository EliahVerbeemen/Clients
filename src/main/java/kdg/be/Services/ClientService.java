package kdg.be.Services;


import kdg.be.Modellen.Client;
import kdg.be.Modellen.Enums.ClientType;
import kdg.be.Modellen.Order;
import kdg.be.Repositories.ClientRepository;
import kdg.be.Services.Interfaces.IClientService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

//TODO @Override
@Component
public class ClientService implements IClientService {
    ClientRepository repo;

    public ClientService(ClientRepository repository) {
        repo = repository;
    }

    public Optional<Client> getClientById(long id) {
        return repo.findById(id);
    }

    @Override
    @Transactional
    public Client updateClient(Client client, Client updated) {

        if (updated.getFamilyName() != null) {
            client.setFamilyName(updated.getFamilyName());
        }
        if (updated.getName() != null) {
            client.setName(updated.getName());
        }
        return repo.save(client);
    }

    @Override
    @Transactional
    public Client updateToB2B(String username) {
        Client client = this.getClientByUsername(username);
        client.setClientType(ClientType.B2B);
        return repo.save(client);
    }

    @Transactional
    public Client addOrderToClient(Client client, Order order) {
        Set<Order> orders = client.getOrders();
        orders.add(order);
        return repo.save(client);
    }

    @Override
    @Transactional
    public void removeClient(long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public Client getClientByUsername(String username) {
        Optional<Client> optionalClient = repo.getClientByEmail(username);
        return optionalClient.orElseGet(() -> repo.save(new Client(username, ClientType.B2C)));
    }


    @Transactional
    public boolean doesClientExist(SecurityContext securityContext) {
        Jwt jwt = (Jwt) securityContext.getAuthentication().getPrincipal();
        String username = (String) jwt.getClaims().get("email");

        Optional<Client> optionalClient = repo.getClientByEmail(username);
        return optionalClient.isPresent();

    }

    @Transactional
    public Client getClientByJwt(SecurityContext securityContext) {
        Jwt jwt = (Jwt) securityContext.getAuthentication().getPrincipal();
        String email = (String) jwt.getClaims().get("email");
        return getClientByUsername(email);
    }

    @Transactional
    public List<Client> getAllClients(){
        return repo.findAll();
    }
}
