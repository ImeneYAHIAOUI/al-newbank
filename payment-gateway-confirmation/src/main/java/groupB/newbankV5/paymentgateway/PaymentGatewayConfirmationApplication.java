package groupB.newbankV5.paymentgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableAspectJAutoProxy
@EnableRedisRepositories
@EnableAsync
@EnableScheduling
public class PaymentGatewayConfirmationApplication {

	public static void main(String[] args) {

		SpringApplication.run(PaymentGatewayConfirmationApplication.class, args);
	}

}
