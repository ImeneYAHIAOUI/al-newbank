export class MetricRequestDto {

    metrics: string[];
    filters: Record<string, string[]>;
    timeRange: TimeRange;
    resolution: string;

    constructor(metrics: string[], filters: Record<string, string[]>, timeRange: TimeRange, resolution: string) {
        this.metrics = metrics;
        this.filters = filters;
        this.timeRange = timeRange;
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