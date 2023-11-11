package groupB.newbankV5.businessIntegrator.repositories;

import groupB.newbankV5.businessIntegrator.entities.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByName(String name);
    Optional<Merchant> findByEmail(String email);
    Optional<Merchant> findByNameOrEmail(String name, String email);
    void deleteAll();


}
