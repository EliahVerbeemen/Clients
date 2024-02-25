package kdg.be.Modellen;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

@Entity
public class Customer implements Serializable {

    // Properties
    @Id
    @GeneratedValue
    public Long klantNumber;

    private int points=0;

    @Enumerated(EnumType.STRING)
    private Klanttype Klanttype;

    @OneToMany(mappedBy = "klant", fetch = FetchType.EAGER)
    private Set<Order> klantOrders=new HashSet<>(); //Dit is een set, overridde equals en hashcode

    // GET & SET
    public Customer.Klanttype getKlanttype() {
        return Klanttype;
    }

    public void setKlanttype(Customer.Klanttype klanttype) {
        Klanttype = klanttype;
    }

    public Set<Order> getOrders() {
        return klantOrders;
    }


    public void setOrders(Set<Order> klantOrders) {
       this.klantOrders  = klantOrders;
    }

    public Long getKlantNumber() {
        return klantNumber;
    }

    public void setKlantNumber(Long klantNumber) {
        klantNumber = klantNumber;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Set<Order> getKlantOrders() {
        return klantOrders;
    }

    public void setKlantOrders(Set<Order> klantOrders) {
        this.klantOrders = klantOrders;
    }

    public enum Klanttype{
        B2B, B2C

    }




    public Customer(int points){
        this.points=points;

    }

    public Customer(Klanttype klanttype){
        this.setKlanttype(klanttype);

    }

public Customer(){
}

public Customer(Klanttype klanttype, int puntenAantal){
        Klanttype=klanttype;
        this.points=puntenAantal;



}

}
