package groupB.newbankV5.businessIntegrator.repositories;

import groupB.newbankV5.businessIntegrator.entities.Application;
import groupB.newbankV5.businessIntegrator.entities.ApplicationAESKey;
import groupB.newbankV5.businessIntegrator.entities.ApplicationKeyPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.PublicKey;
import java.util.Optional;

@Repository
public interface ApplicationKeyPairRepository extends JpaRepository<ApplicationKeyPair, Long> {
    Optional<ApplicationKeyPair> findByApplication(Application application);
    Optional<ApplicationKeyPair> findByPublicKey(PublicKey publicKey);
}
