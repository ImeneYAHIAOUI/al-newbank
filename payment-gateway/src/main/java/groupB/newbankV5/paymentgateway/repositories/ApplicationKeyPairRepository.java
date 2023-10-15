package groupB.newbankV5.paymentgateway.repositories;

import groupB.newbankV5.paymentgateway.entities.Application;
import groupB.newbankV5.paymentgateway.entities.ApplicationKeyPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationKeyPairRepository extends JpaRepository<ApplicationKeyPair, Long> {
    Optional<ApplicationKeyPair> findByApplication(Application application);
    Optional<ApplicationKeyPair> findByPublicKey(String publicKey);
}
