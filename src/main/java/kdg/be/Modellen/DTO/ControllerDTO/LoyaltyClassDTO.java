package kdg.be.Modellen.DTO.ControllerDTO;

import kdg.be.Modellen.LoyalityClass;

public class LoyaltyClassDTO {
    private long loyaltyClassId;
    private String name;
    private int miniumPoints;
    private double reduction;

    public LoyaltyClassDTO(LoyalityClass loyalityClass){
        loyaltyClassId = loyalityClass.getLoyalityClassId();
        name = loyalityClass.getName();
        miniumPoints = loyalityClass.getMinimumPoints();
        reduction = loyalityClass.getReduction();
    }

    public long getLoyaltyClassId() {
        return loyaltyClassId;
    }

    public void setLoyaltyClassId(long loyaltyClassId) {
        this.loyaltyClassId = loyaltyClassId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMiniumPoints() {
        return miniumPoints;
    }

    public void setMiniumPoints(int miniumPoints) {
        this.miniumPoints = miniumPoints;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }
}
