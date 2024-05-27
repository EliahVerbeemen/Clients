package kdg.be.Services.Interfaces;

import kdg.be.Modellen.LoyaltyClass;
import kdg.be.Modellen.Order;

import java.util.List;
import java.util.Set;

public interface ILoyaltyClassService {

    public List<LoyaltyClass> findAll();
    public LoyaltyClass createLoyaltyClass(LoyaltyClass loyaltyClass);
    public LoyaltyClass getLoyaltyClass(int points);
    public List<Order> calculateDiscounts(Set<Order> orders, double reduction);
}
