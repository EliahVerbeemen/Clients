package kdg.be.Services.Interfaces;

import kdg.be.Modellen.Client;
import kdg.be.Modellen.Order;
import org.springframework.security.core.context.SecurityContext;

import java.util.List;
import java.util.Optional;

public interface IClientService {
    public List<Client> getAllClients();
    public Optional<Client> getClientById(long id);
    public Client updateClient(Client client, Client updated);
    public void removeClient(long id);
    public Client getClientByUsername(String username);
    public Client updateToB2B(String  username);
    public Client addOrderToClient(Client client, Order order);
    public boolean doesClientExist(SecurityContext securityContext);
    public Client getClientByJwt(SecurityContext securityContext);
}
