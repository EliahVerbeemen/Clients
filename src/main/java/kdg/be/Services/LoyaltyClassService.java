package kdg.be.Services;

import jakarta.transaction.Transactional;
import kdg.be.Modellen.LoyaltyClass;
import kdg.be.Modellen.Order;
import kdg.be.Repositories.LoyaltyClassRepository;
import kdg.be.Services.Interfaces.ILoyaltyClassService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LoyaltyClassService implements ILoyaltyClassService {
    private final LoyaltyClassRepository loyaltyClassRepository;

    public LoyaltyClassService(LoyaltyClassRepository loyaltyClassRepository) {
        this.loyaltyClassRepository = loyaltyClassRepository;
    }

    public List<LoyaltyClass> findAll() {
        return loyaltyClassRepository.findAll();
    }

    @Transactional
    public LoyaltyClass createLoyaltyClass(LoyaltyClass loyaltyClass) {
        Optional<LoyaltyClass> optionalIsClassPresent = loyaltyClassRepository.findLoyaltyClassesByMinimumPoints(loyaltyClass.getMinimumPoints());
        Optional<LoyaltyClass> optionalLoyaltyClass = loyaltyClassRepository.findLoyaltyClassesByName(loyaltyClass.getName());
        if (optionalIsClassPresent.isPresent() || optionalLoyaltyClass.isPresent()) {
            LoyaltyClass classes = optionalIsClassPresent.orElseGet(optionalLoyaltyClass::get);
            classes.setName(loyaltyClass.getName());
            classes.setReduction(loyaltyClass.getReduction());
            classes.setMinimumPoints(loyaltyClass.getMinimumPoints());
            return loyaltyClassRepository.save(classes);
        }
        return loyaltyClassRepository.save(new LoyaltyClass(loyaltyClass.getName(), loyaltyClass.getMinimumPoints(), loyaltyClass.getReduction()));
    }

    public LoyaltyClass getLoyaltyClass(int points) {
        Optional<LoyaltyClass> optionalClass = loyaltyClassRepository.findAll().stream().filter(e -> e.getMinimumPoints() < points).max(Comparator.comparingInt(LoyaltyClass::getMinimumPoints));
        return optionalClass.orElseGet(() -> loyaltyClassRepository.findAll().stream().min((e, k) -> e.getMinimumPoints()).orElse(new LoyaltyClass()));
    }

    public List<Order> calculateDiscounts(Set<Order> orders, double reduction) {
        return orders.stream().peek(e -> {
            e.setDiscount(e.getTotalPrice() * reduction);
        }).peek(j -> j.setTotalPrice(j.getTotalPrice() - j.getTotalPrice() * reduction)).collect(Collectors.toList());
    }
}
