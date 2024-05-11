package kdg.be.Services.Interfaces;

import kdg.be.Modellen.LoyaltyClass;

import java.util.List;

public interface ILoyalityClassManager {

    public List<LoyaltyClass> findAll();

    public LoyaltyClass save(LoyaltyClass loyaltyClass);

}
