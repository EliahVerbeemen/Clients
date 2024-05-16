package kdg.be.Repositories;

import kdg.be.Modellen.LoyalityClass;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoyalityClassRepository extends JpaRepository<LoyalityClass,Long> {

    public  Optional<LoyalityClass> findLoyalityClassesByName(String name);
    public  Optional<LoyalityClass> findLoyalityClassesByMinimumPoints(int points);
@Override
   public List<LoyalityClass>findAll(Sort sort);
}
