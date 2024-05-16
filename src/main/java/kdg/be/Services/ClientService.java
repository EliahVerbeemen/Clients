package kdg.be.Services;


import kdg.be.Modellen.Enums.ClientType;
import kdg.be.Services.Interfaces.IClientManager;
import kdg.be.Modellen.Client;
import kdg.be.Modellen.Order;
import kdg.be.Repositories.ClientRepository;
import kdg.be.testjes.ClientNotFoundException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.Set;

//TODO @Override
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
    @Transactional
    public Client updateClient(Client client, Client updated) {

        if(updated.getFamilyName()!=null) {
            client.setFamilyName(updated.getFamilyName());
        }
        if(updated.getName()!=null) {
            client.setName(updated.getName());
        }
        return _repo.save(client);
    }

   @Transactional
    public Client addOrderToClient(Client client, Order order) {
        Set<Order> orders=client.getOrders();
    //    Set<Order> ordersTwee=client.getClientOrders();
        orders.add(order);
     //   ordersTwee.add(order);


        return _repo.save(client);
    }


    @Override
    @Transactional
    public void removeClient(long id) {

       _repo.deleteById(id);
    }

    @Override
    @Transactional
    public Client getClientByUsername(String username) {
      Optional<Client> optionalClient=  _repo.getClientByEmail(username);
        return optionalClient.orElseGet(() -> _repo.save(new Client(username,ClientType.B2C)));


    }


    @Transactional
    public boolean doesClientExist(SecurityContext securityContext) {
        Jwt jwt= (Jwt) securityContext.getAuthentication().getPrincipal();
        String username= (String) jwt.getClaims().get("email");

        Optional<Client> optionalClient=  _repo.getClientByEmail(username);
        return optionalClient.isPresent();

    }



    @Transactional
    public Client getClientByJwt(SecurityContext securityContext){

        Jwt jwt= (Jwt) securityContext.getAuthentication().getPrincipal();
      String email= (String) jwt.getClaims().get("email");
        return getClientByUsername(email);

    }

    public Client DoesClientExist(SecurityContext securityContext){

        Jwt jwt= (Jwt) securityContext.getAuthentication().getPrincipal();
        String email= (String) jwt.getClaims().get("email");
        return getClientByUsername(email);

    }

}
