package kdg.be.Modellen;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "clientId")
public class Client implements Serializable {

    // Properties
    @Id
    @GeneratedValue
    public Long clientId;
    private int points = 0;
    @Enumerated(EnumType.STRING)
    private ClientType ClientType;
    @OneToMany(mappedBy = "klant", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Order> clientOrders = new HashSet<>(); //Dit is een set, overridde equals en hashcode

    // GET & SET
    public ClientType getClientType() {
        return ClientType;
    }

    public void setClientType(ClientType clientType) {
        ClientType = clientType;
    }

    public Set<Order> getOrders() {
        return clientOrders;
    }
    public void setOrders(Set<Order> klantOrders) {
        this.clientOrders = klantOrders;
    }
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long klantNumber) {
        this.clientId = klantNumber;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

 /*   public Set<Order> getClientOrders() {
        return clientOrders;
    }

    public void setClientOrders(Set<Order> klantOrders) {
        this.clientOrders = klantOrders;
    }
*/
    public enum ClientType {
        B2B, B2C
    }
    // Constructors
    public Client(int points) {
        this.points = points;
    }
    public Client(ClientType clientType) {
        this.setClientType(clientType);
    }
    public Client() {
    }
    public Client(ClientType clientType, int puntenAantal) {
        ClientType = clientType;
        this.points = puntenAantal;
    }
    public Client(ClientType clientType, int puntenAantal, int clientId) {
        ClientType = clientType;
        this.points = puntenAantal;
        this.clientId= (long) clientId;
    }
    @Override
    public   String toString(){

        return "id:"+ clientId +"type" + getClientType() + "punten" + this.getPoints();

    }

}
