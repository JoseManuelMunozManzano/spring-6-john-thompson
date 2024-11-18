package com.jmunoz.restmvc.repositories;

import com.jmunoz.restmvc.entities.BeerAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerAuditRepository extends JpaRepository<BeerAuditEntity, UUID> {
}
