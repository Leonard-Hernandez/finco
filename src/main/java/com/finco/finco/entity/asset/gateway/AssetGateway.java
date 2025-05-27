package com.finco.finco.entity.asset.gateway;

import java.util.List;
import java.util.Optional;

import com.finco.finco.entity.asset.model.Asset;
import com.finco.finco.entity.user.model.User;

public interface AssetGateway {

    Asset create(Asset asset);
    Asset update(Asset asset);
    void delete(Asset asset);

    Optional<Asset> findById(Long id);

    List<Asset> findAllByUser(User used);

    List<Asset> findByUserAndNameLike(User user, String search);

}
