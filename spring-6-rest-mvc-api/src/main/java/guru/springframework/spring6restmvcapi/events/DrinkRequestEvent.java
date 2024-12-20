package guru.springframework.spring6restmvcapi.events;

import guru.springframework.spring6restmvcapi.model.BeerOrderLineDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Enviaremos una petición DrinkRequestEvent a un microservicio que la procesará, y devolverá un DrinkPreparedEvent.
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrinkRequestEvent {

    private BeerOrderLineDto beerOrderLineDto;
}
