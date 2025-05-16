package com.finco.finco.entity.asset.gateway;

import java.util.List;
import java.util.Optional;

import com.finco.finco.entity.asset.model.Asset;

public interface AssetGateway {

    Asset create(Asset asset);
    Asset update(Asset asset);
    void delete(Asset asset);

    Optional<Asset> findById(Long id);

    List<Asset> findByUserIdAndNameLike(Long userId, String search);

}
