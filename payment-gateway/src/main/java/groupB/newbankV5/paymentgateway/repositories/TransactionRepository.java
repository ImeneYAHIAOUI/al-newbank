package groupB.newbankV5.paymentgateway.repositories;



import groupB.newbankV5.paymentgateway.entities.Transaction;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends CassandraRepository<Transaction, UUID> {

}
