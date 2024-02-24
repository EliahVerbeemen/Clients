package kdg.be.Modellen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;

import java.io.Serializable;
import java.util.*;

@Entity
public class Klant implements Serializable {

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
    public Klant.Klanttype getKlanttype() {
        return Klanttype;
    }

    public void setKlanttype(Klant.Klanttype klanttype) {
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




    public Klant(int points){
        this.points=points;

    }

    public Klant(Klanttype klanttype){
        this.setKlanttype(klanttype);

    }

public Klant(){
}

public Klant(Klanttype klanttype, int puntenAantal){
        Klanttype=klanttype;
        this.points=puntenAantal;



}

}
