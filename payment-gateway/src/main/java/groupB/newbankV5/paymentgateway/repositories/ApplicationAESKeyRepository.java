package groupB.newbankV5.paymentgateway.repositories;

import groupB.newbankV5.paymentgateway.entities.Application;
import groupB.newbankV5.paymentgateway.entities.ApplicationAESKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationAESKeyRepository extends JpaRepository<ApplicationAESKey, Long> {
    Optional<ApplicationAESKey> findByApplication(Application application);
    Optional<ApplicationAESKey> findByAesKey(String aesKey);
}
