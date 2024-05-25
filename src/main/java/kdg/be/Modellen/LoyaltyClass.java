package kdg.be.Modellen;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public  class LoyaltyClass {

    //Properties
    @Id
    @GeneratedValue
    private Long loyaltyClassId;
    private String name;
    private int minimumPoints;
    //Waarde tussen 0 en 1;
    private double reduction;

    //GET & SET
    public String getName() {
        return name;
    }

    public void setName(String naam) {
        this.name = naam;
    }

    public Long getLoyaltyClassId() {
        return loyaltyClassId;
    }

    public void setLoyaltyClassId(Long id) {
        this.loyaltyClassId = id;
    }

    public int getMinimumPoints() {
        return minimumPoints;
    }

    public void setMinimumPoints(int minimumPoints) {
        this.minimumPoints = minimumPoints;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    // Constructors
    public LoyaltyClass(String name, int minimPoints, double reduction) {
      this.name=name;
      this.minimumPoints =minimPoints;
      this.reduction=reduction;
    }

    public LoyaltyClass() {
    }
}
