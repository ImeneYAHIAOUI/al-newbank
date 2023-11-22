package groupB.newbankV5.paymentgateway.controllers.dto;

public class TransferResponseDto {

    private boolean success;
    private String message;

    public TransferResponseDto() {
    }

    public TransferResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
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
}
