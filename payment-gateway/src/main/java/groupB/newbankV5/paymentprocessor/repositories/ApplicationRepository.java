package groupB.newbankV5.paymentprocessor.repositories;

import groupB.newbankV5.paymentprocessor.entities.Application;
import groupB.newbankV5.paymentprocessor.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByUrl(String url);
    Optional<Application> findByName(String name);
    Optional<Application> findByNameOrUrl(String name, String url);
}
