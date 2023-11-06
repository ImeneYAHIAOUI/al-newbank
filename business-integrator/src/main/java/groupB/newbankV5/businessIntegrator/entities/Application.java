package groupB.newbankV5.businessIntegrator.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import groupB.newbankV5.businessIntegrator.components.Integrator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Application {
    @Id
    @GeneratedValue
    @Column(name = "Application_id", nullable = false)
    private Long id;
    private String name;
    private String email;
    private String url;
    private String description;

    @Column(unique = true, length = 2048)
    private String apiKey;

    @OneToOne(mappedBy = "application")
    private Merchant merchant;

    public Application() {
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Application(String name, String email, String url, String description) {
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

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String generateToken(){
        String token =  Jwts.builder()
                .setIssuer("NewBank")
                .setSubject("API Key")
                .setExpiration(Date.from(java.time.Instant.now().plusSeconds(3600)))
                .claim("id", this.id)
                .claim("name", this.name)
                .claim("email", this.email)
                .claim("url", this.url)
                .claim("description", this.description)
                .claim("dateOfIssue", System.currentTimeMillis())
                .signWith(SignatureAlgorithm.HS256, Integrator.SECRET_KEY)
                .compact();
        this.setApiKey(token);
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(url, that.url) && Objects.equals(description, that.description) && Objects.equals(apiKey, that.apiKey) && Objects.equals(merchant, that.merchant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, url, description, apiKey, merchant);
    }
    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", merchant=" + merchant +
                '}';
    }
}
