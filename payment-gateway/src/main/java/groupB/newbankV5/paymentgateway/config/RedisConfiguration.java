package groupB.newbankV5.paymentgateway.config;

import groupB.newbankV5.paymentgateway.entities.ApplicationKeyPair;
import groupB.newbankV5.paymentgateway.entities.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    private final RedisConnectionFactory redisConnectionFactory1;
    private final RedisConnectionFactory redisConnectionFactory2;

    public RedisConfiguration(
            @Value("${spring.redis1.host}") String redisHost1,
            @Value("${spring.redis1.port}") int redisPort1,
            @Value("${spring.redis2.host}") String redisHost2,
            @Value("${spring.redis2.port}") int redisPort2
    ) {
        this.redisConnectionFactory1 = createJedisConnectionFactory(redisHost1, redisPort1);
        this.redisConnectionFactory2 = createJedisConnectionFactory(redisHost2, redisPort2);
    }

    @Bean
    public RedisTemplate<String, Transaction> redisTemplate1() {
        RedisTemplate<String, Transaction> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory1);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Transaction> redisTemplate2() {
        RedisTemplate<String, Transaction> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory2);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }


    @Bean
    public RedisTemplate<String, ApplicationKeyPair> redisTemplateKeyPair1() {
        RedisTemplate<String, ApplicationKeyPair> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory1);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, ApplicationKeyPair> redisTemplateKeyPair2() {
        RedisTemplate<String, ApplicationKeyPair> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory2);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }
    private RedisConnectionFactory createJedisConnectionFactory(String host, int port) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(host);
        connectionFactory.setPort(port);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }
}

