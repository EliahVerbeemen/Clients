package kdg.be.Modellen;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@JsonSerialize
public class Order {

    // Properties
    @Id
    @GeneratedValue
    private Long orderNumber;

    @ManyToOne(optional = true)
    @JoinColumn(   name = "klant_id")
    @JsonBackReference
    private Klant klant;

    private LocalDate BestelDatum;
  //  @JsonSerialize(keyUsing = OrdersDeserialisatie.class)

    @Transient
    public double Korting;

    public double Totaalprijs;

    private BestellingStatus BestellingsStatus;

    @ElementCollection
    private Map<Product,Integer> Producten=new HashMap<>();

    //Berekende kolom prijs
    // GET & SET
    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Klant getKlant() {
        return klant;
    }

    public void setKlant(Klant klant) {
        this.klant = klant;
    }

    public LocalDate getBestelDatum() {
        return BestelDatum;
    }

    public void setBestelDatum(LocalDate bestelDatum) {
        BestelDatum = bestelDatum;
    }

    public Map<Product, Integer> getProducten() {
        return Producten;
    }

    public void setProducten(Map<Product, Integer> producten) {
        Producten = producten;
    }

//Voor communicatie naar de klant

    public double getTotaalprijs() {
        return Totaalprijs;
    }

    public void setTotaalprijs(double totaalprijs) {
        Totaalprijs = totaalprijs;
    }

    public double getKorting() {
        return Korting;
    }

    public void setKorting(double korting) {
        Korting = korting;
    }

    public BestellingStatus getBestellingsStatus() {
        return BestellingsStatus;
    }

    public void setBestellingsStatus(BestellingStatus bestellingsStatus) {
        BestellingsStatus = bestellingsStatus;
    }

    // Constructors
    public Order(Klant klant, LocalDate bestelDatum, Map<Product, Integer> producten, BestellingStatus bestellingsStatus) {

        this.klant = klant;
        BestelDatum = bestelDatum;
        Producten = producten;
        BestellingsStatus = bestellingsStatus;
    }

    public Order(){

    }
    public Order(Klant klant){

    }

    public enum BestellingStatus {
        Niet_bevestigd,
        Bevestigd,
        Geannulleerd
    }
}
