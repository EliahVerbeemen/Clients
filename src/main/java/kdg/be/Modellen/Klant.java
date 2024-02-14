package kdg.be.Modellen;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kdg.be.SerializatieHelpers.SetSerialisatie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
public class Klant implements Serializable {

    @Id
    @GeneratedValue
    public Long klantNumber;


    public Klant.Klanttype getKlanttype() {
        return Klanttype;
    }

    public void setKlanttype(Klant.Klanttype klanttype) {
        Klanttype = klanttype;
    }


    @Enumerated(EnumType.STRING)
    private Klanttype Klanttype;

    public enum Klanttype{
        B2B, B2C

    }
    private int PuntenAantal=0;

    //Dit is een set, overridde equals en hashcode

   @OneToMany(mappedBy = "klant")
    private Set<Order> Orders;


    public Set<Order> getOrders() {
        return Orders;
    }

    public void setOrders(Set<Order> orders) {
        Orders = orders;
    }

    @Transient
    public  static   List<Loyaliteitsklasse> LoyliteitsKlassen= new ArrayList<Loyaliteitsklasse>() {
        {new Loyaliteitsklasse("Brons",0,0.00);}


    };

    public Long getKlantNumber() {
        return klantNumber;
    }

    public void setKlantNumber(Long klantNumber) {
        klantNumber = klantNumber;
    }



    public int getPuntenAantal() {
        return PuntenAantal;
    }

    public void setPuntenAantal(int puntenAantal) {
        PuntenAantal = puntenAantal;
    }










public Klant(){
}

public Klant(Klanttype klanttype, int puntenAantal){
        Klanttype=klanttype;
        PuntenAantal=puntenAantal;



}

}
