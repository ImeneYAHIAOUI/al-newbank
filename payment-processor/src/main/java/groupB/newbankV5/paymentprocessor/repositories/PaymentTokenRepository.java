package groupB.newbankV5.paymentprocessor.repositories;

import groupB.newbankV5.paymentprocessor.entities.PaymentToken;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTokenRepository extends CassandraRepository<PaymentToken, String> {

}
