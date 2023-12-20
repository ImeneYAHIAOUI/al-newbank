package groupB.newbankV5.paymentprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableCassandraRepositories
@EnableAspectJAutoProxy
@EnableScheduling
public class PaymentProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentProcessorApplication.class, args);
	}

}
