package kdg.be.Modellen;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

@Entity
public class Client implements Serializable {

    // Properties
    @Id
    @GeneratedValue
    public Long clientId;
    private int points = 0;
    @Enumerated(EnumType.STRING)
    private ClientType ClientType;
    @OneToMany(mappedBy = "klant", fetch = FetchType.EAGER)
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
        klantNumber = klantNumber;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Set<Order> getClientOrders() {
        return clientOrders;
    }

    public void setClientOrders(Set<Order> klantOrders) {
        this.clientOrders = klantOrders;
    }

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

}
