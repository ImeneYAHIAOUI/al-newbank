package groupB.newbankV5.transactions.repositories;

import groupB.newbankV5.transactions.entities.Transaction;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

public interface TransactionRepository extends CassandraRepository<Transaction, Long> {

}
