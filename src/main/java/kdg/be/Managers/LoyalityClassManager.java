package kdg.be.Managers;

import kdg.be.Modellen.LoyalityClasses;
import kdg.be.Repositories.LoyalityClassRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LoyalityClassManager implements ILoyalityClassManager {


    private LoyalityClassRepository loyalityClassRepository;

    public LoyalityClassManager(LoyalityClassRepository loyalityClassRepository){
        this.loyalityClassRepository=loyalityClassRepository;


    }
    public List<LoyalityClasses> findAll(){
       return loyalityClassRepository.findAll(Sort.by("minimumPoints"));
    }

    public LoyalityClasses save(LoyalityClasses loyalityClasses){

        Optional<LoyalityClasses> optioneelIsKlasseAlPresent=loyalityClassRepository.findAll().stream().filter(e->e.getMinimumPoints()==loyalityClasses.getMinimumPoints()).findFirst();
            optioneelIsKlasseAlPresent.ifPresent(classes -> loyalityClassRepository.delete(classes));
        return loyalityClassRepository.save(loyalityClasses);
    }

}
