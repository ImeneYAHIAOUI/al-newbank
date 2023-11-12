package groupB.newbankV5.paymentgateway.repositories;

import groupB.newbankV5.paymentgateway.entities.ApplicationKeyPair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ApplicationKeyPairRepository {

    private final RedisTemplate<String, ApplicationKeyPair> redisTemplate1;
    private final RedisTemplate<String, ApplicationKeyPair> redisTemplate2;

    public ApplicationKeyPairRepository(
            @Qualifier("redisTemplateKeyPair1") RedisTemplate<String, ApplicationKeyPair> redisTemplate1,
            @Qualifier("redisTemplateKeyPair2") RedisTemplate<String, ApplicationKeyPair> redisTemplate2
    ) {
        this.redisTemplate1 = redisTemplate1;
        this.redisTemplate2 = redisTemplate2;
    }


    public void save(ApplicationKeyPair value) {
        UUID id = value.getId();

        int hashCode = id.hashCode();

        if (hashCode % 2 == 0) {
            redisTemplate1.opsForValue().set(id.toString(), value);
        } else {
            redisTemplate2.opsForValue().set(id.toString(), value);
        }
    }

    public ApplicationKeyPair findById(UUID id) {
        int hashCode = id.hashCode();

        if (hashCode % 2 == 0)
            return redisTemplate1.opsForValue().get(id.toString());

        return redisTemplate2.opsForValue().get(id.toString());
    }

    public Optional<ApplicationKeyPair> findByApplicationName(String name) {

        Optional<ApplicationKeyPair> optionalApplicationKeyPair=findApplicationName(redisTemplate1, name);
        if(optionalApplicationKeyPair.isEmpty()){
            optionalApplicationKeyPair=findApplicationName(redisTemplate2, name);
        }
        return optionalApplicationKeyPair;
    }

    private Optional<ApplicationKeyPair> findApplicationName(RedisTemplate<String, ApplicationKeyPair> redisTemplate, String name) {
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            Object object = redisTemplate.opsForValue().get(key);
            if (object != null && object instanceof ApplicationKeyPair) {
                ApplicationKeyPair pair = (ApplicationKeyPair) object;
                if (pair.getApplicationName().equals(name)) {
                    return Optional.of(pair);
                }
            }
        }
        return Optional.empty();
    }

}