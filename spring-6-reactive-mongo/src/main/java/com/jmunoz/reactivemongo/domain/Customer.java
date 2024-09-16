package com.jmunoz.reactivemongo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

// Importante la anotación @Document para MongoDB, para que la entidad sea un documento de Mongo.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Customer {

    // Mongo requiere que el id sea String
    @Id
    private String id;

    private String customerName;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
