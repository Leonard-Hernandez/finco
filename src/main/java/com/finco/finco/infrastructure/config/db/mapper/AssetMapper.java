package com.finco.finco.infrastructure.config.db.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.asset.model.Asset;
import com.finco.finco.infrastructure.config.db.schema.AssetSchema;

@Component
public class AssetMapper {

    private final UserMapper userMapper;

    public AssetMapper(@Lazy UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Asset toAsset(AssetSchema assetSchema) {
        if (assetSchema == null) {
            return null;
        }

        Asset asset = new Asset();
        asset.setId(assetSchema.getId());
        asset.setUser(userMapper.toLightUser(assetSchema.getUser()));
        asset.setName(assetSchema.getName());
        asset.setEstimatedValue(assetSchema.getEstimatedValue());
        asset.setEstimatedValue(assetSchema.getEstimatedValue());
        asset.setInterestRate(assetSchema.getInterestRate());
        asset.setDescription(assetSchema.getDescription());

        return asset;
    }

    public AssetSchema toAssetSchema(Asset asset) {
        if (asset == null) {
            return null;
        }

        if (asset.getUser() == null || asset.getUser().getId() == null) {
            throw new IllegalArgumentException("Asset must be associated with an existing User (with an ID).");
        }

        AssetSchema assetSchema = new AssetSchema();
        assetSchema.setId(asset.getId());
        assetSchema.setUser(userMapper.toLightUserSchema(asset.getUser()));
        assetSchema.setName(asset.getName());
        assetSchema.setEstimatedValue(asset.getEstimatedValue());
        assetSchema.setEstimatedValue(asset.getEstimatedValue());
        assetSchema.setInterestRate(asset.getInterestRate());
        assetSchema.setDescription(asset.getDescription());

        return assetSchema;
    }

}
