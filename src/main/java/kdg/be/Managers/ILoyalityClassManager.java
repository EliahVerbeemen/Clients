package kdg.be.Managers;

import kdg.be.Modellen.LoyalityClasses;

import java.util.List;

public interface ILoyalityClassManager {

    public List<LoyalityClasses> findAll();

    public LoyalityClasses save(LoyalityClasses loyalityClasses);

}
