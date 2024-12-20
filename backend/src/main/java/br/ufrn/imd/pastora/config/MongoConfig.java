
package br.ufrn.imd.pastora.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port}")
    private String mongoPort;

    @Value("${spring.data.mongodb.username}")
    private String mongoUser;

    @Value("${spring.data.mongodb.password}")
    private String mongoPassword;

    @Value("${spring.data.mongodb.database}")
    private String mongoDbName;

    @Bean 
    MongoClient mongoClient() {
        StringBuilder uri = new StringBuilder(String.format("%s:%s/%s", mongoHost, mongoPort, mongoDbName));

        final boolean hasUser = mongoUser !=null && !mongoUser.isBlank();
        final boolean hasPassword = mongoPassword != null && !mongoPassword.isBlank();

        if(hasUser && hasPassword) {
            uri.insert(0, String.format("%s:%s@", mongoUser, mongoPassword));
        }

        uri.insert(0, "mongodb://");
        return MongoClients.create(uri.toString());
    }
}
