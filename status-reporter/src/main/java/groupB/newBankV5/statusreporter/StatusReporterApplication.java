package groupB.newBankV5.statusreporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class StatusReporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatusReporterApplication.class, args);
	}

}
