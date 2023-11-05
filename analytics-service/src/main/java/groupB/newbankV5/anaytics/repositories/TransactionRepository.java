package groupB.newbankV5.anaytics.repositories;


import groupB.newbankV5.anaytics.entities.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}