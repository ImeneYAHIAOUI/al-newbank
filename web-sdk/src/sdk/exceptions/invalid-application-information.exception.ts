export class InvalidApplicationInformationException extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'InvalidApplicationInformationException';
    }
}
