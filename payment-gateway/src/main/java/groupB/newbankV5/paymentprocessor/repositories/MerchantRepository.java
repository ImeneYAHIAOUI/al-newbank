package groupB.newbankV5.paymentprocessor.repositories;

import groupB.newbankV5.paymentprocessor.entities.Merchant;
import groupB.newbankV5.paymentprocessor.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByName(String name);
    Optional<Merchant> findByEmail(String email);
    Optional<Merchant> findByNameOrEmail(String name, String email);
}
