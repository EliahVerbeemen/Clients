package kdg.be.Repositories;

import kdg.be.Modellen.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {

    //Hij verwacht een attribuut die start met een kleine letter
    public void deleteKlantByKlantNumber(Long id);



}
