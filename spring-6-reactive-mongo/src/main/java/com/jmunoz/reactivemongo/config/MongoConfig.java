package com.jmunoz.reactivemongo.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

import static java.util.Collections.singletonList;

// Esta es la configuración base para conectarnos a una BD de MongoDB.
//@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Value("${spring.data.mongo.username}")
    private String username;

    @Value("${spring.data.mongo.database}")
    private String database;

    @Value("${spring.data.mongo.password}")
    private String password;

    @Value("${spring.data.mongo.address}")
    private String address;

    @Value("${spring.data.mongo.port}")
    private Integer port;

    // Configuración de un bean que devuelve un MongoClient
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    // Esta es la BD que vamos a usar en Mongo
    @Override
    protected String getDatabaseName() {
        return "sfg";
    }

    // Esta autenticación hace falta si tenemos (como yo) MongoDB en Docker, ya que hay usuario/contraseña.
    // No haría falta para MongoDB en local, ya que, probablemente, no tiene usuario/contraseña.
    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        // Importante al password aplicarle la función .toCharArray()
        builder.credential(MongoCredential.createCredential(username, database, password.toCharArray()))
                .applyToClusterSettings(settings -> {
                    settings.hosts(singletonList(
                            new ServerAddress(address, port)
                    ));
                });
    }
}
