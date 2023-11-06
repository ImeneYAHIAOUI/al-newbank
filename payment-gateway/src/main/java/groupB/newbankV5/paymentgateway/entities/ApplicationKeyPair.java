package groupB.newbankV5.paymentgateway.entities;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;
import java.util.UUID;

@Table
public class ApplicationKeyPair {

    @PrimaryKey
    private UUID id;


    @CassandraType(type = CassandraType.Name.BLOB)
    private ByteBuffer publicKey;
    @CassandraType(type = CassandraType.Name.BLOB)
    private ByteBuffer privateKey;


    @Indexed
    private String applicationName;

    public ApplicationKeyPair() {
    }

    public ApplicationKeyPair(ByteBuffer publicKey, ByteBuffer privateKey, String applicationName) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.applicationName = applicationName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ByteBuffer getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(ByteBuffer publicKey) {
        this.publicKey = publicKey;
    }

    public ByteBuffer getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(ByteBuffer privateKey) {
        this.privateKey = privateKey;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationKeyPair that = (ApplicationKeyPair) o;
        return Objects.equals(publicKey, that.publicKey) && Objects.equals(privateKey, that.privateKey) && Objects.equals(applicationName, that.applicationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicKey, privateKey, applicationName);
    }
}
