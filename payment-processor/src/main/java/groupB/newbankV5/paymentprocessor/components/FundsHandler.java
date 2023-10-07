package groupB.newbankV5.paymentprocessor.components;

import groupB.newbankV5.paymentprocessor.controllers.dto.PaymentDetailsDTO;
import org.springframework.stereotype.Component;

@Component
public class FundsHandler {

        public boolean hasSufficientFunds(PaymentDetailsDTO paymentDetailsDTO) {
            return true;
        }

        public void deductFunds(double amount) {
        }
}
