package com.finco.finco.infrastructure.config.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finco.finco.infrastructure.config.db.schema.AssetSchema;

@Repository
public interface AssetRepository extends JpaRepository<AssetSchema, Long> {

    List<AssetSchema> findByUserIdAndNameLike(Long userId, String search);

}
