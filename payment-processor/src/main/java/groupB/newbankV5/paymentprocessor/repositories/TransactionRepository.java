package groupB.newbankV5.paymentprocessor.repositories;

import groupB.newbankV5.paymentprocessor.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
