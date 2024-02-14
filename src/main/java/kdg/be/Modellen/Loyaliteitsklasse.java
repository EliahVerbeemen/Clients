package kdg.be.Modellen;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public final class Loyaliteitsklasse {

    private String naam;

    private int minimumPuntenAantal;

    private double korting;

    public Long getLoyaliteitsklasseId() {
        return LoyaliteitsklasseId;
    }

    public void setLoyaliteitsklasseId(Long loyaliteitsklasseId) {
        LoyaliteitsklasseId = loyaliteitsklasseId;
    }

    @Id
    @GeneratedValue
    private Long LoyaliteitsklasseId;


    public Loyaliteitsklasse(String naam, int minimumPuntenAantal, double korting) {
        this.naam = naam;
        this.minimumPuntenAantal = minimumPuntenAantal;
        this.korting = korting;
    }

    public Loyaliteitsklasse() {

    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public int getMinimumPuntenAantal() {
        return minimumPuntenAantal;
    }

    public void setMinimumPuntenAantal(int minimumPuntenAantal) {
        this.minimumPuntenAantal = minimumPuntenAantal;
    }

    public double getKorting() {
        return korting;
    }

    public void setKorting(double korting) {
        this.korting = korting;
    }
}
