package groupB.newbankV5.feescalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FeesCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeesCalculatorApplication.class, args);
	}

}
