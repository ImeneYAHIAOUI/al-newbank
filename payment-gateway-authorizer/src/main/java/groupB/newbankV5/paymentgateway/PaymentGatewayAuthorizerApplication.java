package groupB.newbankV5.paymentgateway;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableAspectJAutoProxy
@EnableRedisRepositories(basePackages = "groupB.newbankV5.paymentgateway.repositories")
@EnableJpaRepositories(basePackages = "groupB.newbankV5.paymentgateway.repository")
@EnableAsync
@EnableScheduling
public class PaymentGatewayAuthorizerApplication {

	public static void main(String[] args) {

		SpringApplication.run(PaymentGatewayAuthorizerApplication.class, args);
	}

	@Bean
@ConfigurationProperties("spring.datasource.hikari")
@Primary
public DataSource dataSource(DataSourceProperties dataSourceProperties){
    final HikariDataSource dataSource = (HikariDataSource) dataSourceProperties.initializeDataSourceBuilder()
            .username(dataSourceProperties.getUsername())
            .password(dataSourceProperties.getPassword())
            .build();
    dataSource.setRegisterMbeans(true);
    dataSource.setConnectionTimeout(10000l); //10 secs
    return dataSource;
}

}
