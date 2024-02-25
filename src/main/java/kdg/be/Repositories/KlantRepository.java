package kdg.be.Repositories;

import kdg.be.Modellen.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KlantRepository extends JpaRepository<Customer,Long> {

    //Hij verwacht een attribuut die start met een kleine letter
    public void deleteKlantByKlantNumber(Long id);



}
