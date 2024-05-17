package kdg.be.Services.Interfaces;

import kdg.be.Modellen.LoyalityClass;

import java.util.List;

public interface ILoyalityClassManager {

    public List<LoyalityClass> findAll();

    public LoyalityClass save(LoyalityClass loyalityClass);


}
