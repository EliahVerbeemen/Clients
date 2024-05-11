package kdg.be.Modellen.DTO.ControllerDTO;

import kdg.be.Modellen.LoyaltyClass;

public class LoyaltyClassDTO {
    private long loyaltyClassId;
    private String name;
    private int miniumPoints;
    private double reduction;

    public LoyaltyClassDTO(LoyaltyClass loyaltyClass){
        loyaltyClassId = loyaltyClass.getLoyaltyClassId();
        name = loyaltyClass.getName();
        miniumPoints = loyaltyClass.getMinimumPoints();
        reduction = loyaltyClass.getReduction();
    }
}
