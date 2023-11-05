package groupB.newbankV5.businessIntegrator.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class ApplicationAESKey {
    @Id
    @GeneratedValue
    @Column(name = "ApplicationAESKey_id", nullable = false)
    private Long id;

    @Column(length = 256)
    private byte[] aesKey;

    @OneToOne
    private Application application;

    public ApplicationAESKey() {
    }

    public ApplicationAESKey(byte[] aesKey, Application application) {
        this.aesKey = aesKey;
        this.application = application;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getAesKey() {
        return aesKey;
    }

    public void setAesKey(byte[] aesKey) {
        this.aesKey = aesKey;
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
        ApplicationAESKey that = (ApplicationAESKey) o;
        return Objects.equals(aesKey, that.aesKey) && Objects.equals(application, that.application);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aesKey, application);
    }
}

