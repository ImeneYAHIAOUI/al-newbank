package groupB.newbankV5.paymentgateway.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@RedisHash("ApplicationKeyPair")
public class ApplicationKeyPair  implements Serializable {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    private UUID id;

    private String publicKey; // Serialized as a String
    private String privateKey; // Serialized as a String

    @Indexed
    private String applicationName;

    public ApplicationKeyPair() {
    }

    public ApplicationKeyPair(ByteBuffer publicKey, ByteBuffer privateKey, String applicationName) {
        this.publicKey = serializeByteBuffer(publicKey);
        this.privateKey = serializeByteBuffer(privateKey);
        this.applicationName = applicationName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ByteBuffer getPublicKey() {
        return deserializeByteBuffer(publicKey);
    }

    public void setPublicKey(ByteBuffer publicKey) {
        this.publicKey = serializeByteBuffer(publicKey);
    }

    public ByteBuffer getPrivateKey() {
        return deserializeByteBuffer(privateKey);
    }

    public void setPrivateKey(ByteBuffer privateKey) {
        this.privateKey = serializeByteBuffer(privateKey);
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

    // Helper methods for serialization and deserialization

    private static String serializeByteBuffer(ByteBuffer buffer) {
        return Base64.getEncoder().encodeToString(buffer.array());
    }

    private static ByteBuffer deserializeByteBuffer(String serialized) {
        byte[] byteArray = Base64.getDecoder().decode(serialized);
        return ByteBuffer.wrap(byteArray);
    }
}
