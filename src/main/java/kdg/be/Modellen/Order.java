package kdg.be.Modellen;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Order {

    // Properties
    @Id
    @GeneratedValue
    private Long orderNumber;

    @ManyToOne(optional = true,cascade =CascadeType.ALL,fetch = FetchType.EAGER)
   // @JoinColumn(   name = "klant_id")
    @JsonBackReference
    private Klant klant;


    private LocalDate bestelDatum;
  //  @JsonSerialize(keyUsing = OrdersDeserialisatie.class)

    @Transient
    public double Korting;

    public double Totaalprijs;

    private BestellingStatus BestellingsStatus;

    @ElementCollection(fetch=FetchType.EAGER)
    private Map<Long,Integer> producs=new HashMap<>();

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
        return bestelDatum;
    }

    public void setBestelDatum(LocalDate bestelDatum) {
        this.bestelDatum = bestelDatum;
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

    public Map<Long, Integer> getProducs() {
        return producs;
    }

    public void setProducs(Map<Long, Integer> producs) {
        this.producs = producs;
    }

    public List<String> getRemarks() {
        return Remarks;
    }

    public void setRemarks(List<String> remarks) {
        Remarks = remarks;
    }

    @ElementCollection
    private List<String> Remarks=new ArrayList<>();
    // Constructors
    public Order(Klant klant, LocalDate bestelDatum, Map<Long, Integer> producten, BestellingStatus bestellingsStatus) {

        this.klant = klant;
        this.bestelDatum = bestelDatum;
        producs = producten;
        BestellingsStatus = bestellingsStatus;
    }

    public Order( Map<Long, Integer> producten, Klant klant) {

        this.klant = klant;
        producs = producten;

    }

    public Order(){

    }


    public enum BestellingStatus {
        Niet_bevestigd,
        Bevestigd,
        Geannulleerd
    }
}
