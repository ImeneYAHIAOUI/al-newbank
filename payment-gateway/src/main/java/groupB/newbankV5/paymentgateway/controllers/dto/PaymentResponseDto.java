package groupB.newbankV5.paymentgateway.controllers.dto;

public class PaymentResponseDto {
    private boolean success;
    private String message;

    public PaymentResponseDto() {
    }

    public PaymentResponseDto(boolean success, String message) {
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

    @Override
    public String toString() {
        return "PaymentResponseDto{" +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
