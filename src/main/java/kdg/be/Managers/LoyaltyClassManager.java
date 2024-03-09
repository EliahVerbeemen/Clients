package kdg.be.Managers;

import kdg.be.Managers.Interfaces.ILoyalityClassManager;
import kdg.be.Modellen.LoyalityClasses;
import kdg.be.Repositories.LoyalityClassRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class LoyaltyClassManager implements ILoyalityClassManager {


    private LoyalityClassRepository loyalityClassRepository;

    public LoyaltyClassManager(LoyalityClassRepository loyalityClassRepository) {
        this.loyalityClassRepository = loyalityClassRepository;


    }

    public List<LoyalityClasses> findAll() {
        return loyalityClassRepository.findAll(Sort.by("minimumPoints"));
    }

    public LoyalityClasses save(LoyalityClasses loyalityClasses) {

        Optional<LoyalityClasses> optioneelIsKlasseAlPresent = loyalityClassRepository.findAll().stream().filter(e -> e.getMinimumPoints() == loyalityClasses.getMinimumPoints()).findFirst();
        optioneelIsKlasseAlPresent.ifPresent(classes -> loyalityClassRepository.delete(classes));
        return loyalityClassRepository.save(loyalityClasses);
    }

    public LoyalityClasses getLoyaltyClass(int points) {
        Optional<LoyalityClasses> optionalClass = loyalityClassRepository.findAll().stream().filter(e -> e.getMinimumPoints() < points).max(Comparator.comparingInt(LoyalityClasses::getMinimumPoints));
        return optionalClass.orElseGet(() -> loyalityClassRepository.findAll().stream().min((e, k) -> e.getMinimumPoints()).orElse(new LoyalityClasses()));
    }

}
