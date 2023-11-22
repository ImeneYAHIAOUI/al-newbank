package groupB.newbankV5.paymentgateway.controllers.dto;

public class ApplicationIntegrationDto {
    private String name;
    private String email;
    private String url;
    private String description;
    private Long merchantId;
    public ApplicationIntegrationDto() {
    }

    public ApplicationIntegrationDto(String name, String email, String url, String description, Long merchantId) {
        this.name = name;
        this.email = email;
        this.url = url;
        this.description = description;
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    @Override
    public String toString() {
        return "ApplicationIntegrationDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", merchantId='" + merchantId + '\'' +
                '}';
    }
}
