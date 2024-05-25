package kdg.be.Modellen;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import kdg.be.Modellen.Enums.ClientType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Order> clientOrders = new HashSet<>();
    private String email;
    private String name;
    private String familyName;

    public Client(String username, ClientType clientType) {
        this.email = username;
        this.ClientType = clientType;
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

    public Client(ClientType clientType, int pointAmount, String email) {
        ClientType = clientType;
        this.points = pointAmount;
        this.email = email;
    }

    public Client(ClientType clientType, int pointAmount, int clientId) {
        ClientType = clientType;
        this.points = pointAmount;
        this.clientId = (long) clientId;
    }

    public Client(String name, String familyName) {
        this.name = name;
        this.familyName = familyName;
    }

    // GET & SET
    public Set<Order> getClientOrders() {
        return clientOrders;
    }
    public void setClientOrders(Set<Order> clientOrders) {
        this.clientOrders = clientOrders;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
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
    public String getFamilyName() {
        return familyName;
    }
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "id:" + clientId + "type" + getClientType() + "punten" + this.getPoints();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;

        if (!clientId.equals(client.clientId)) return false;
        return email.equals(client.email);
    }

    @Override
    public int hashCode() {
        int result = clientId.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}
