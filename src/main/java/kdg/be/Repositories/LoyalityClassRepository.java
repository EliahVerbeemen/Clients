package kdg.be.Repositories;

import kdg.be.Modellen.LoyalityClasses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoyalityClassRepository extends JpaRepository<LoyalityClasses,Long> {


}
