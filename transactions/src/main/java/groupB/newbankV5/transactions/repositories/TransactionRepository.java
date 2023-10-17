package groupB.newbankV5.transactions.repositories;

import groupB.newbankV5.transactions.entities.Trans;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface TransactionRepository extends CassandraRepository<Trans, Long> {

}
