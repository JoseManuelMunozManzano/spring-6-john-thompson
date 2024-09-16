package com.jmunoz.reactivemongo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    // Mongo requiere que el id sea String
    private String id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String customerName;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
