package com.jmunoz.restmvc.repositories;

import com.jmunoz.restmvc.entities.BeerOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerOrderRepository extends JpaRepository<BeerOrderEntity, UUID> {
}
