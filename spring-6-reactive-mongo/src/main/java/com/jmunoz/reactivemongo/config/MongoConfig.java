package com.jmunoz.reactivemongo.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

// Esta es la configuración base para conectarnos a una BD de MongoDB.
// Falta configurar la autenticación
@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {

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
}
