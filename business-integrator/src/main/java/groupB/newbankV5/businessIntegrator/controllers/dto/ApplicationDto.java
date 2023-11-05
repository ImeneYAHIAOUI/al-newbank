package groupB.newbankV5.businessIntegrator.controllers.dto;

import groupB.newbankV5.businessIntegrator.entities.Application;
import groupB.newbankV5.businessIntegrator.entities.Merchant;

import javax.persistence.Column;

public class ApplicationDto {

    private Long id;
    private String name;
    private String email;
    private String url;
    private String description;

    private String apiKey;

    private MerchantDto merchant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public MerchantDto getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantDto merchant) {
        this.merchant = merchant;
    }

    public static ApplicationDto applicationDtoFactory(Application application) {
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setId(application.getId());
        applicationDto.setName(application.getName());
        applicationDto.setEmail(application.getEmail());
        applicationDto.setUrl(application.getUrl());
        applicationDto.setDescription(application.getDescription());
        applicationDto.setApiKey(application.getApiKey());
        applicationDto.setMerchant(MerchantDto.merchantDtoFactory(application.getMerchant()));
        return applicationDto;
    }

}
