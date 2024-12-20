package guru.springframework.spring6restmvcapi.events;

import guru.springframework.spring6restmvcapi.model.BeerOrderLineDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrinkPreparedEvent {

    private BeerOrderLineDto beerOrderLine;
}
