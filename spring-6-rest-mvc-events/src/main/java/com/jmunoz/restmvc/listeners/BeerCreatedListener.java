package com.jmunoz.restmvc.listeners;

import com.jmunoz.restmvc.events.BeerCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// Esta forma de tener los listeners, donde cada clase contiene un event, es autocontenida
// y centrada en un evento concreto.

// Necesitamos escanear este componente al contexto de Spring
@Component
public class BeerCreatedListener {

    // Para habilitar este méto-do como un listener, tenemos que añadir esta anotación
    @EventListener
    public void listen(BeerCreatedEvent event) {
        System.out.println("I heard a beer was created!");
        System.out.println(event.getBeer().getId());

        // TODO: add real implementation to persist audit record
    }
}
