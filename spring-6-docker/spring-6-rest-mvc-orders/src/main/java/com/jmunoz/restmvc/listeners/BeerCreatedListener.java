package com.jmunoz.restmvc.listeners;

import com.jmunoz.restmvc.events.*;
import com.jmunoz.restmvc.mappers.BeerMapper;
import com.jmunoz.restmvc.repositories.BeerAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

// Esta forma de tener los listeners, donde cada clase contiene un event, es autocontenida
// y centrada en un evento concreto.
// Necesitamos escanear este componente al contexto de Spring, de ahí el @Component
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerCreatedListener {

    private final BeerMapper beerMapper;
    private final BeerAuditRepository beerAuditRepository;

    // Para habilitar este méto-do como un listener, tenemos que añadir la anotación @EventListener
    //
    // Para habilitar la asincronía, también tenemos que añadir la anotación @Async
    //
    // Sin utilizar la interfase BeerEvent
//    @Async
//    @EventListener
//    public void listen(BeerCreatedEvent event) {
//        // System.out.println("I heard a beer was created!");
//        // System.out.println(event.getBeer().getId());
//
//        System.out.println("Current Thread Name: " + Thread.currentThread().getName());
//        System.out.println("Current Thread ID: " + Thread.currentThread().getId());
//
//        val beerAudit = beerMapper.beerEntityToBeerAuditEntity(event.getBeer());
//        beerAudit.setAuditEventType("BEER_CREATED");
//
//        // Autenticación
//        if (event.getAuthentication() != null && event.getAuthentication().getName() != null) {
//            beerAudit.setPrincipalName(event.getAuthentication().getName());
//        }
//
//        val savedBeerAudit = beerAuditRepository.save(beerAudit);
//        log.debug("Beer Audit Saved: " + savedBeerAudit.getId());
//    }

    // En vez de crear una clase para cada tipo de evento (update, patch, delete y create) se
    // ha generalizado usando la interface BeerEvent.
    @Async
    @EventListener
    public void listen(BeerEvent event) {
        
        val beerAudit = beerMapper.beerEntityToBeerAuditEntity(event.getBeer());
        
        String eventType = null;
        
        switch (event) {
            case BeerCreatedEvent beerCreatedEvent -> eventType = "BEER_CREATED";
            case BeerPatchEvent beerPatchEvent -> eventType = "BEER_UPDATED";
            case BeerUpdatedEvent beerUpdatedEvent -> eventType = "BEER_PATCH";
            case BeerDeletedEvent beerDeletedEvent -> eventType = "BEER_DELETE";
            default -> eventType = "UNKNOWN";
        }

        beerAudit.setAuditEventType(eventType);

        if (event.getAuthentication() != null && event.getAuthentication().getName() != null) {
            beerAudit.setPrincipalName(event.getAuthentication().getName());
        }

        val savedBeerAudit = beerAuditRepository.save(beerAudit);
        log.debug("Beer Audit Saved: " + eventType + " " + savedBeerAudit.getId());
    }
}
