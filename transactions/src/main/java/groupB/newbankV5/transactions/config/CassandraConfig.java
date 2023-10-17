package groupB.newbankV5.transactions.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableCassandraRepositories(basePackages = "groupB.newbankV5.transactions.entities")
public class CassandraConfig extends AbstractCassandraConfiguration {

    private static final String KEYSPACE = "my_keyspace";


    @Override
    protected String getKeyspaceName() {
        return KEYSPACE; // Change this to your desired keyspace name
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"groupB.newbankV5.transactions.entities"};
    }


    @Override
    public String getContactPoints() {
        try {
            InetAddress address = InetAddress.getByName("cassandra");
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            // Handle the exception
            return "localhost"; // Provide a default IP address
        }
    }

    @Override
    protected int getPort() {
        return 9042; // Cassandra's default port
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace
                        (KEYSPACE)
                .ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication());
    }

}



