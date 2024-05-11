package kdg.be.Services;

import kdg.be.Services.Interfaces.ILoyalityClassManager;
import kdg.be.Modellen.LoyaltyClass;
import kdg.be.Repositories.LoyalityClassRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class LoyaltyClassService implements ILoyalityClassManager {


    private LoyalityClassRepository loyalityClassRepository;

    public LoyaltyClassService(LoyalityClassRepository loyalityClassRepository) {
        this.loyalityClassRepository = loyalityClassRepository;


    }

    public List<LoyaltyClass> findAll() {
        return loyalityClassRepository.findAll(Sort.by("minimumPoints"));
    }

    public LoyaltyClass save(LoyaltyClass loyaltyClass) {

      Optional<LoyaltyClass> optioneelIsKlasseAlPresent = loyalityClassRepository.findLoyalityClassesByMinimumPoints(loyaltyClass.getMinimumPoints());
       if (optioneelIsKlasseAlPresent.isPresent()) {
           LoyaltyClass classes = optioneelIsKlasseAlPresent.get();
           classes.setName(loyaltyClass.getName());
           classes.setReduction(loyaltyClass.getReduction());
           classes.setMinimumPoints(loyaltyClass.getMinimumPoints());
           return loyalityClassRepository.save(classes);
       }

      Optional<LoyaltyClass> optionalLoyalityClass=loyalityClassRepository.findLoyalityClassesByName(loyaltyClass.getName());
        if(optionalLoyalityClass.isPresent()){
            LoyaltyClass loyaltyClass1 =optionalLoyalityClass.get();
            loyaltyClass1.setReduction(loyaltyClass.getReduction());
            loyaltyClass1.setName(loyaltyClass.getName());
            return loyalityClassRepository.save(loyaltyClass1);

        }
        return loyalityClassRepository.save(loyaltyClass);
    }

    public LoyaltyClass getLoyaltyClass(int points) {
        Optional<LoyaltyClass> optionalClass = loyalityClassRepository.findAll().stream().filter(e -> e.getMinimumPoints() < points).max(Comparator.comparingInt(LoyaltyClass::getMinimumPoints));
        return optionalClass.orElseGet(() -> loyalityClassRepository.findAll().stream().min((e, k) -> e.getMinimumPoints()).orElse(new LoyaltyClass()));
    }



}
