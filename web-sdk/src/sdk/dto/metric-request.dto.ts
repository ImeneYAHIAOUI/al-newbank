export class MetricRequestDto {

    metrics: string[];
    filters: Record<string, string[]>;
    timeRange: TimeRange;
    period: string;
    resolution: string;

    constructor(metrics: string[], filters: Record<string, string[]>, timeRange: TimeRange, period: string, resolution: string) {
        this.metrics = metrics;
        this.filters = filters;
        this.timeRange = timeRange;
        this.period = period;
        this.resolution = resolution;
    }
}


export class TimeRange {
    start: string;
    end: string;

    constructor(start: string, end: string) {
        this.start = start;
        this.end = end;
    }
}