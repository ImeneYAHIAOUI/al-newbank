package groupB.newbankV5.paymentprocessor.repositories;



import groupB.newbankV5.paymentprocessor.entities.Transaction;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CassandraRepository<Transaction, Long> {

}
