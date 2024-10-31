package com.jmunoz.restmvc.events;

import com.jmunoz.restmvc.entities.BeerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BeerUpdatedEvent implements BeerEvent {

    private BeerEntity beer;

    private Authentication authentication;
}
