package groupB.newbankV5.externalbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ExternalBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalBankApplication.class, args);
    }

}