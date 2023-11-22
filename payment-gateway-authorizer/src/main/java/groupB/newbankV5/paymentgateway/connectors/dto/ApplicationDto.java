package groupB.newbankV5.paymentgateway.connectors.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import groupB.newbankV5.paymentgateway.controllers.dto.MerchantDto;
import io.jsonwebtoken.Jwts;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;


public class ApplicationDto {

     Long id;
     String name;
     String email;
     String url;
     String description;

     String apiKey;

    private MerchantDto merchant;

    public ApplicationDto() {
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public ApplicationDto(String name, String email, String url, String description) {
        this.name = name;
        this.email = email;
        this.url = url;
        this.description = description;
    }

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

    public MerchantDto getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantDto merchant) {
        this.merchant = merchant;
    }

}
