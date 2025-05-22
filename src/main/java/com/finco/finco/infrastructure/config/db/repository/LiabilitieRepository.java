package com.finco.finco.infrastructure.config.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finco.finco.infrastructure.config.db.schema.LiabilitieSchema;

@Repository
public interface LiabilitieRepository extends JpaRepository<LiabilitieSchema, Long> {

}
