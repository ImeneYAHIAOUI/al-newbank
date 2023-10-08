package groupB.newbankV5.paymentprocessor.repositories;

import groupB.newbankV5.paymentprocessor.entities.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}
