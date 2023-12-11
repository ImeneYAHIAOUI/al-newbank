export class RequestDto {

    dateTime: string; // Assuming LocalDateTime is represented as a string
    time: number; // Assuming BigDecimal is represented as a number
    status: string;
    details: string;



    constructor(  dateTime: string, time: number, status: string, details: string) {
        this.dateTime = dateTime;
        this.time = time;
        this.status = status;
        this.details = details;
    }



}