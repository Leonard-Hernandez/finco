package com.finco.finco.infrastructure.config.db.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.asset.model.Asset;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.AssetSchema;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

@Component
public class AssetMapper {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public AssetMapper(@Lazy UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public Asset toAsset(AssetSchema assetSchema) {
        if (assetSchema == null) {
            return null;
        }

        Asset asset = new Asset();
        asset.setId(assetSchema.getId());
        asset.setUser(userMapper.toLigthUser(assetSchema.getUser()));
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

        UserSchema userSchema = null;
        User userDomain = asset.getUser();

        if (userDomain == null || userDomain.getId() == null) {
            throw new IllegalArgumentException("Asset must be associated with an existing User (with an ID).");
        }

        userSchema = userRepository.findById(userDomain.getId())
                .orElseThrow(() -> new RuntimeException("user not found"));

        AssetSchema assetSchema = new AssetSchema();
        assetSchema.setId(asset.getId());
        assetSchema.setUser(userSchema);
        assetSchema.setName(asset.getName());
        assetSchema.setEstimatedValue(asset.getEstimatedValue());
        assetSchema.setEstimatedValue(asset.getEstimatedValue());
        assetSchema.setInterestRate(asset.getInterestRate());
        assetSchema.setDescription(asset.getDescription());

        return assetSchema;
    }

}
