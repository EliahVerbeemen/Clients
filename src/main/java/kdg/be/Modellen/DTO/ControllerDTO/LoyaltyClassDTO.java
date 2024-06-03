package kdg.be.Modellen.DTO.ControllerDTO;

import kdg.be.Modellen.LoyaltyClass;

public class LoyaltyClassDTO {
    private long loyaltyClassId;
    private String name;
    private int minimumPoints;
    private double reduction;

    public LoyaltyClassDTO(LoyaltyClass loyaltyClass){
        loyaltyClassId = loyaltyClass.getLoyaltyClassId();
        name = loyaltyClass.getName();
        minimumPoints = loyaltyClass.getMinimumPoints();
        reduction = loyaltyClass.getReduction();
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
}
