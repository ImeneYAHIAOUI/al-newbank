package groupB.newbankV5.customercare.repositories;

import groupB.newbankV5.customercare.entities.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
}
