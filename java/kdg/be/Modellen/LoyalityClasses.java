package kdg.be.Modellen;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

//Moet beheerbaar zijn
@Entity
public  class LoyalityClasses {

    //Properties
    @Id
    @GeneratedValue
    private Long loyalityClassId;
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

    public Long getLoyalityClassId() {
        return loyalityClassId;
    }

    public void setLoyalityClassId(Long loyalityClassId) {
        this.loyalityClassId = loyalityClassId;
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
    public LoyalityClasses(String name, int minimPoints, double reduction) {
      this.name=name;
      this.minimumPoints =minimPoints;
      this.reduction=reduction;

    }

    public LoyalityClasses() {

    }


}
