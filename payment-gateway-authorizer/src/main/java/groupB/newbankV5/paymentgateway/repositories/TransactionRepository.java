package groupB.newbankV5.paymentgateway.repositories;

import groupB.newbankV5.paymentgateway.entities.Transaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class TransactionRepository {

    private final RedisTemplate<String, Transaction> redisTemplate1;
    private final RedisTemplate<String, Transaction> redisTemplate2;

    public TransactionRepository(
            @Qualifier("redisTemplate1") RedisTemplate<String, Transaction> redisTemplate1,
            @Qualifier("redisTemplate2") RedisTemplate<String, Transaction> redisTemplate2
    ) {
        this.redisTemplate1 = redisTemplate1;
        this.redisTemplate2 = redisTemplate2;
    }

    public void save(Transaction transaction) {
        UUID id = transaction.getId();
        int hashCode = id.hashCode();

        if (hashCode % 2 == 0) {
            redisTemplate1.opsForValue().set(id.toString(), transaction);
        } else {
            redisTemplate2.opsForValue().set(id.toString(), transaction);
        }
    }

    public Transaction findById(UUID id) {
        int hashCode = id.hashCode();

        if (hashCode % 2 == 0) {
            return redisTemplate1.opsForValue().get(id.toString());
        } else {
            return redisTemplate2.opsForValue().get(id.toString());
        }
    }


}
