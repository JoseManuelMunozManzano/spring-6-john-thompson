package com.jmunoz.restmvc.repositories;

import com.jmunoz.restmvc.entities.BeerOrderLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLineEntity, UUID> {
}
