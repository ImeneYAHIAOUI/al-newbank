export class ErrorDto {
    error: string;
    details: string;
  
    constructor(error: string, details: string) {
        this.error = error;
        this.details = details;
    }
}