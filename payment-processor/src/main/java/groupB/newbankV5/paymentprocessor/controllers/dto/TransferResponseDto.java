package groupB.newbankV5.paymentprocessor.controllers.dto;

public class TransferResponseDto {

    private boolean success;
    private String message;

    private String authToken;

    public TransferResponseDto() {
    }

    public TransferResponseDto(boolean success, String message, String authToken) {
        this.success = success;
        this.message = message;
        this.authToken = authToken;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
