export class InvalidMerchantInformationException extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'InvalidMerchantInformationException';
    }
}
