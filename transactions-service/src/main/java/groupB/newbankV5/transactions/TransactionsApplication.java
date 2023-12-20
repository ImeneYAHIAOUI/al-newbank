package groupB.newbankV5.transactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableAspectJAutoProxy
@EnableScheduling
public class TransactionsApplication {

    public static void main(String[] args) {

        SpringApplication.run(TransactionsApplication.class, args);
    }


    public void rebuild(){};
}
