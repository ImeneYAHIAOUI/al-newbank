package groupB.newbankV5.feescalculator.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
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
                '}';
    }
}
