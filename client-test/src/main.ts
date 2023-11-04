import { IntegrationService } from '@imeneyahiaoui/newbank-sdk';
import { PaymentService } from '@imeneyahiaoui/newbank-sdk';

async function main() {
    const loadBalancerHost = 'localhost';
    const integrationService = new IntegrationService(loadBalancerHost);
    const applicationInfo = {
        IBAN: 'FR7879719882953',
        BIC: 'KMKMHK06',
        name: 'app2',
        email: 'dskrezzerzazeoej@gsld.com',
        url: 'https://rtzer"yujzazeazer-rty.com',
        description: 'My App description',
    };
    const paymentInfo = {
        cardNumber: '4101234567890123',
        cvv: '734',
        expirationDate: '11/2025',
        amount: '50',
    };
    const result = await integrationService.integrateService(applicationInfo);
    const paymentService = new PaymentService(loadBalancerHost);
    await paymentService.processCardInfo(paymentInfo, result.id, result.apiKey);
}
main();