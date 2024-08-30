package com.jmunoz.spring6datar2dbc.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

// Hay que habilitar de forma explícita los campos de auditoría de las entidades para que se devuelva correctamente
//// la data de los campos createdDate y lastModifiedDate.
@Configuration
@EnableR2dbcAuditing
public class DatabaseConfig {

    // Hay que configurar el recurso schema.sql (donde se crea la tabla en BD H2, en memoria) que luego utilizamos.
    @Value("classpath:/schema.sql")
    Resource resource;

    // Spring Boot configura la conexión a la BD automáticamente.
    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(resource));
        return initializer;
    }
}
