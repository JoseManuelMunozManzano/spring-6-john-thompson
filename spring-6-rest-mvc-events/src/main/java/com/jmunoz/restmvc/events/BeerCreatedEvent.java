package com.jmunoz.restmvc.events;

import com.jmunoz.restmvc.entities.BeerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;

// Aunque es un POJO, estamos usando BeerEntity, que a su vez tiene referencias a otras entidades,
// y esto puede causar un bucle circular infinito al usar .toString(), .equals() y .hashcode() junto con Lombok.
// Es un problema de Lombok, y, para evitarlo, es que uso @Getter y @Setter, para así no obtener lo que me
// puede causar problemas.
//
// Al final, lo que quiero es escribir un evento de auditoría cada vez que BeerEntity sea modificado en alguna forma.
@Getter
@Setter
@AllArgsConstructor
@Builder
public class BeerCreatedEvent {

    private BeerEntity beer;

    private Authentication authentication;
}
