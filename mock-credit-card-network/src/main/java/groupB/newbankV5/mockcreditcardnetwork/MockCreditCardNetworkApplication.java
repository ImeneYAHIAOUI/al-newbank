package groupB.newbankV5.mockcreditcardnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MockCreditCardNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockCreditCardNetworkApplication.class, args);
    }

}
