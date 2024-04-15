package kdg.be.Repositories;

import kdg.be.Modellen.Client;
import kdg.be.Modellen.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client,Long> {

    //Hij verwacht een attribuut die start met een kleine letter
  /*  @Transactional
    public Client deleteClientBy(Long id);
*/



}
