const { Client } = require('pg');

// Replace these values with your actual database connection details
const connectionString = 'postgresql://postgresuser:postgrespass@35.209.89.240:5432/transaction-db';
const query = 'SELECT * FROM transactions'

// Create a new PostgreSQL client
const client = new Client({
    connectionString: connectionString,
});

// Connect to the PostgreSQL database
client.connect();

// Execute the query
client.query(query, (err, result) => {
    if (err) {
        console.error('Error executing query', err);
    } else {
        // Process the query result
        console.log('Query result:', result.rows);
    }

    // Disconnect from the PostgreSQL database
    client.end();
});
