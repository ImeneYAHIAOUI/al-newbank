package groupB.newbankV5.transactions.repositories;

import groupB.newbankV5.transactions.entities.Transaction;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface TransactionRepository extends CassandraRepository<Transaction, Long> {

}
