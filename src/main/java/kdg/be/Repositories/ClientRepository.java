package kdg.be.Repositories;

import kdg.be.Modellen.Client;
import kdg.be.Modellen.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    @Transactional
    public Optional<Client> getClientByEmail(String email);
}
