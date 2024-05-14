package kdg.be.Services;

import kdg.be.Modellen.Order;
import kdg.be.Modellen.Product;
import kdg.be.Services.Interfaces.ILoyalityClassManager;
import kdg.be.Modellen.LoyalityClass;
import kdg.be.Repositories.LoyalityClassRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LoyalityClassService implements ILoyalityClassManager {


    private LoyalityClassRepository loyalityClassRepository;

    public LoyalityClassService(LoyalityClassRepository loyalityClassRepository) {
        this.loyalityClassRepository = loyalityClassRepository;


    }

    public List<LoyalityClass> findAll() {
        return loyalityClassRepository.findAll(/*Sort.by("reduction")*/);
    }

    public LoyalityClass save(LoyalityClass loyalityClass) {

      Optional<LoyalityClass> optioneelIsKlasseAlPresent = loyalityClassRepository.findLoyalityClassesByMinimumPoints(loyalityClass.getMinimumPoints());
       if (optioneelIsKlasseAlPresent.isPresent()) {
           LoyalityClass classes = optioneelIsKlasseAlPresent.get();
           classes.setName(loyalityClass.getName());
           classes.setReduction(loyalityClass.getReduction());
           classes.setMinimumPoints(loyalityClass.getMinimumPoints());
           return loyalityClassRepository.save(classes);
       }

      Optional<LoyalityClass> optionalLoyalityClass=loyalityClassRepository.findLoyalityClassesByName(loyalityClass.getName());
        if(optionalLoyalityClass.isPresent()){
            LoyalityClass loyalityClass1 =optionalLoyalityClass.get();
            loyalityClass1.setReduction(loyalityClass.getReduction());
            loyalityClass1.setName(loyalityClass.getName());
            return loyalityClassRepository.save(loyalityClass1);

        }
        return loyalityClassRepository.save(loyalityClass);
    }

    public LoyalityClass getLoyaltyClass(int points) {
        Optional<LoyalityClass> optionalClass = loyalityClassRepository.findAll().stream().filter(e -> e.getMinimumPoints() < points).max(Comparator.comparingInt(LoyalityClass::getMinimumPoints));
        return optionalClass.orElseGet(() -> loyalityClassRepository.findAll().stream().min((e, k) -> e.getMinimumPoints()).orElse(new LoyalityClass()));
    }

    public List<Order> calculatDiscounts(Set<Order>orders, double reduction) {
    return    orders.stream().peek(e->{e.setDiscount(e.getTotalPrice()*reduction);}).peek(j->j.setTotalPrice(j.getTotalPrice()-j.getTotalPrice()*reduction)).collect(Collectors.toList());

    }


}
