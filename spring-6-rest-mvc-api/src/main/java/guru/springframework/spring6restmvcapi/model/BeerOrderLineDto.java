package guru.springframework.spring6restmvcapi.model;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
@Builder
@Data
public class BeerOrderLineDto {
    private UUID id;

    private Long version;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;

    private BeerDto beer;

    @Min(value = 1, message = "Quantity On Hand must be greater than 0")
    private Integer orderQuantity;
    private Integer quantityAllocated;
}
