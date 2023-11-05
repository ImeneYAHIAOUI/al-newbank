import { IntegrationService } from '@teamb/newbank-sdk';
import { PaymentService } from '@teamb/newbank-sdk';

async function main() {
    const loadBalancerHost = 'localhost';
      const paymentService = new PaymentService(loadBalancerHost);
      const paymentInfo = {
        cardNumber: '4101234567890123',
        cvv: '734',
        expirationDate: '11/2025',
        amount: '50',
      };
    await paymentService.processCardInfo(paymentInfo, result.id);
}
main();