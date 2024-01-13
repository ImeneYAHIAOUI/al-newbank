export class MetricsDto {
    values: Record<string, number>;
    timestamp: string;

    constructor(values: Record<string, number>, timestamp: string) {
        this.values = values;
        this.timestamp = timestamp;
    }
}