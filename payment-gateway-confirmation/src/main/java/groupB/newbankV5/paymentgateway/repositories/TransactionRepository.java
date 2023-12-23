package groupB.newbankV5.paymentgateway.repositories;

import groupB.newbankV5.paymentgateway.entities.Transaction;
import groupB.newbankV5.paymentgateway.entities.TransactionStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        String key = transaction.toString();
        if (hashCode % 2 == 0) {
            deleteById(redisTemplate1, transaction.getId());
            redisTemplate1.opsForValue().set(key, transaction);
        } else {
            deleteById(redisTemplate2, transaction.getId());
            redisTemplate2.opsForValue().set(key, transaction);
        }
    }


    public List<Transaction> findByStatus(TransactionStatus status) {
        List<Transaction> transactions = findByStatus(redisTemplate1, status);
        transactions.addAll(findByStatus(redisTemplate2, status));
        return transactions;
    }

    public List<Transaction> findByStatus(RedisTemplate<String, Transaction> redisTemplate, TransactionStatus status){
        List<Transaction> transactions = new ArrayList<>();
        String pattern = "*status=" + status + "*";
        List<String> keys = scanKeys(pattern, redisTemplate);
        for(String key : keys) {
            Transaction t = redisTemplate.opsForValue().get(key);
            transactions.add(t);
        }
        return transactions;
    }

    public List<String> scanKeys(String pattern, RedisTemplate<String, Transaction> redisTemplate) {
        return redisTemplate.execute((RedisCallback<List<String>>) connection -> connection.scan(ScanOptions.scanOptions().match(pattern).build())
                .stream()
                .map(keyBytes -> new String(keyBytes, StandardCharsets.UTF_8))
                .collect(Collectors.toList()));
    }


    public Transaction findById(UUID id) {
        int hashCode = id.hashCode();
        if (hashCode % 2 == 0) {
            return findById(redisTemplate1, id);
        } else {
            return findById(redisTemplate2, id);
        }
    }

    public Transaction findById(RedisTemplate<String, Transaction> redisTemplate, UUID id){
        String pattern = "*id='" + id + "'*";
        List<String> keys = scanKeys(pattern, redisTemplate);
        Optional<String> key = keys.stream().findAny();
        return key.map(s -> redisTemplate.opsForValue().get(s)).orElse(null);

    }

    public void deleteById(RedisTemplate<String, Transaction> redisTemplate, UUID id) {
        String pattern = "*id='" + id + "'*";
        Optional<String> key = scanKeys(pattern, redisTemplate).stream().findAny();
        key.ifPresent(redisTemplate::delete);
    }
}