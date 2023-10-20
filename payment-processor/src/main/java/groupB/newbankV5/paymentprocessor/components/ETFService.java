package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.interfaces.IDirectDepositor;
import org.springframework.stereotype.Component;
import java.util.Random;
@Component
public class ETFService implements IDirectDepositor {

    @Override
    public boolean validateTransaction(String accountNumber, double amount) {

        return new Random().nextBoolean();
    }
}
