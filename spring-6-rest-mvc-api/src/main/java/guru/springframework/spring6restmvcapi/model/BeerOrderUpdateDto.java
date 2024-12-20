package guru.springframework.spring6restmvcapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeerOrderUpdateDto {
    private String customerRef;

    @NotNull
    private UUID customerId;

    private Set<BeerOrderLineUpdateDto> beerOrderLines;

    private BeerOrderShipmentUpdateDto beerOrderShipment;

    private BigDecimal paymentAmount;
}
