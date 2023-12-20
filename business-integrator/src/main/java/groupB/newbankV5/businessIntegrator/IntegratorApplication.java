package groupB.newbankV5.businessIntegrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableAspectJAutoProxy
@EnableScheduling
public class IntegratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegratorApplication.class, args);
	}

}
