package kdg.be.Services.Interfaces;

import kdg.be.Modellen.Client;
import org.springframework.security.core.context.SecurityContext;

import java.util.Optional;

public interface IClientManager {
    public Optional<Client> getClientById(long id);

    public Client updateClient(Client client, Client updated);

    public void removeClient(long id);

    public Client getClientByUsername(String username);

    public Client updateToB2B(String  username);
}
