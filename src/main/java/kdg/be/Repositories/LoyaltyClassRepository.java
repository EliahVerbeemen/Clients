package kdg.be.Repositories;

import kdg.be.Modellen.LoyaltyClass;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoyaltyClassRepository extends JpaRepository<LoyaltyClass,Long> {

    public  Optional<LoyaltyClass> findLoyalityClassesByName(String name);
    public  Optional<LoyaltyClass> findLoyalityClassesByMinimumPoints(int points);
@Override
   public List<LoyaltyClass>findAll(Sort sort);
}
