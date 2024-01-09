package groupB.newbankV5.metrics.repositories;

import groupB.newbankV5.metrics.entities.RedisTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RedisTransactionRepository extends MongoRepository<RedisTransaction, String> {

}
