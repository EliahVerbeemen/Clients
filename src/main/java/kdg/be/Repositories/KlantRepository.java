package kdg.be.Repositories;

import kdg.be.Modellen.Klant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KlantRepository extends JpaRepository<Klant,Long> {

    //Hij verwacht een attribuut die start met een kleine letter
    public void deleteKlantByKlantNumber(Long id);



}
