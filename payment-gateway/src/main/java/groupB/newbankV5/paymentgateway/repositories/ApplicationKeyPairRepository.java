package groupB.newbankV5.paymentgateway.repositories;

import groupB.newbankV5.paymentgateway.entities.ApplicationKeyPair;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationKeyPairRepository extends CrudRepository<ApplicationKeyPair, String> {
        Optional<ApplicationKeyPair> findByApplicationName(String applicationName);
}
