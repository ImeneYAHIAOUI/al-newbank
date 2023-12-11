package groupB.newbankV5.metrics.repositories;

import groupB.newbankV5.metrics.entities.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findByRecipientIBAN(String recipientIBAN);





}
