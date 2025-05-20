package com.finco.finco.infrastructure.config.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finco.finco.infrastructure.config.db.schema.LiabilitieSchema;

public interface LiabilitieRepository extends JpaRepository<LiabilitieSchema, Long> {

}
