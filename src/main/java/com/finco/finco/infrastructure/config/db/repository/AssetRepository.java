package com.finco.finco.infrastructure.config.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finco.finco.infrastructure.config.db.schema.AssetSchema;

public interface AssetRepository extends JpaRepository<AssetSchema, Long> {

    List<AssetSchema> findByUserIdAndNameLike(Long userId, String search);

}
