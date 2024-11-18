package com.jmunoz.restmvc.events;

import com.jmunoz.restmvc.entities.BeerEntity;
import org.springframework.security.core.Authentication;

public interface BeerEvent {

    BeerEntity getBeer();

    Authentication getAuthentication();
}
