
package br.ufrn.imd.pastora.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    public @Bean MongoClient mongoClient() {
        return MongoClients.create("mongodb://pastora:pastora@localhost:27017");
    }
}
