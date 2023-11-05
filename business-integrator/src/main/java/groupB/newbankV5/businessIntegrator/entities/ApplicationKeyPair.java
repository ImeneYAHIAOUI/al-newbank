package groupB.newbankV5.businessIntegrator.entities;

import javax.persistence.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;

@Entity
public class ApplicationKeyPair {
    @Id
    @GeneratedValue
    @Column(name = "ApplicationKeyPair_id", nullable = false)
    private Long id;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    @OneToOne
    private Application application;

    public ApplicationKeyPair() {
    }

    public ApplicationKeyPair(PublicKey publicKey, PrivateKey privateKey, Application application) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.application = application;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationKeyPair that = (ApplicationKeyPair) o;
        return Objects.equals(publicKey, that.publicKey) && Objects.equals(privateKey, that.privateKey) && Objects.equals(application, that.application);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicKey, privateKey, application);
    }
}
