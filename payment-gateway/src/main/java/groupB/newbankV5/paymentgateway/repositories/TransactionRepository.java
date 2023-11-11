package groupB.newbankV5.paymentgateway.repositories;



import groupB.newbankV5.paymentgateway.entities.Transaction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, UUID> {

}
