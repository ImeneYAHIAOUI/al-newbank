package groupB.newbankV5.paymentgateway.repositories;


import groupB.newbankV5.paymentgateway.entities.ApplicationKeyPair;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.security.PublicKey;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationKeyPairRepository extends CassandraRepository<ApplicationKeyPair, UUID> {
    Optional<ApplicationKeyPair> findByApplicationName(String applicationName);
}
