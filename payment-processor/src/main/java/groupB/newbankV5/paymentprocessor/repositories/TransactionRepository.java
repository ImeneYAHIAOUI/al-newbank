package groupB.newbankV5.paymentprocessor.repositories;

import groupB.newbankV5.paymentprocessor.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
