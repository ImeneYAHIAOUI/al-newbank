package groupB.newbankV5.businessIntegrator.repositories;

import groupB.newbankV5.businessIntegrator.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByUrl(String url);
    Optional<Application> findByName(String name);
    Optional<Application> findByNameOrUrl(String name, String url);
}
