package kdg.be.Repositories;

import kdg.be.Modellen.LoyaltyClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoyalityClassRepository extends JpaRepository<LoyaltyClass,Long> {

    public  Optional<LoyaltyClass> findLoyalityClassesByName(String name);
    public  Optional<LoyaltyClass> findLoyalityClassesByMinimumPoints(int points);


}
