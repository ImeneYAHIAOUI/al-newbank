package groupB.newBankV5.statusreporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableCaching
public class StatusReporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatusReporterApplication.class, args);
	}

}
