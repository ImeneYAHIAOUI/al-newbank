package groupB.newbankV5.paymentprocessor.connectors.dto;

public class CcnResponseDto {

    boolean response;
    String message;

    public CcnResponseDto() {
    }
    public CcnResponseDto(boolean response) {
        this.response = response;
    }

    public CcnResponseDto(boolean response, String message) {
        this.response = response;
        this.message = message;
    }

    public boolean getResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
