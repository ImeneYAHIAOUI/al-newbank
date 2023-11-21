package groupB.newbankV5.anaytics.controllers.dto;

public class ErrorDTO {
    private String error;

    private String details;

    public ErrorDTO(String error) {
        this.error = error;
    }

    public ErrorDTO() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}