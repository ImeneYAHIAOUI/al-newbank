package groupB.newbankV5.metrics.entities;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Document(collection = "requests")
public class Request {

    @Id
    private String id;

    @Indexed
    private Long application;

    private LocalDateTime dateTime;
    private BigDecimal time;
    private String status;
    private String details;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getApplication() {
        return application;
    }

    public void setApplication(Long application) {
        this.application = application;
    }

    public BigDecimal getTime() {
        return time;
    }

    public void setTime(BigDecimal time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id='" + id + '\'' +
                ", application=" + application +
                ", dateTime=" + dateTime +
                ", time=" + time +
                ", status='" + status + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
