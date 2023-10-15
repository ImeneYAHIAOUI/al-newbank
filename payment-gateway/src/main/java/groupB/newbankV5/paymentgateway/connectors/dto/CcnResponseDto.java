package groupB.newbankV5.paymentgateway.connectors.dto;

public class CcnResponseDto {

    boolean response;
    String message;
    String authToken;
    public CcnResponseDto() {
    }
    public CcnResponseDto(boolean response) {
        this.response = response;
    }

    public CcnResponseDto(boolean response, String message) {
        this.response = response;
        this.message = message;
    }

    public CcnResponseDto(boolean response, String message, String authToken) {
        this.response = response;
        this.message = message;
        this.authToken = authToken;
    }

    public boolean isApproved() {
        return response;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
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
