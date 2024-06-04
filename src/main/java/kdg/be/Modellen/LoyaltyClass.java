package kdg.be.Modellen;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@Entity
public  class LoyaltyClass {

    //Properties
    @Id
    @GeneratedValue
    private Long loyaltyClassId;
    private String name;
    private int minimumPoints;
    private double reduction;

    //GET & SET
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLoyaltyClassId() {
        return loyaltyClassId;
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
