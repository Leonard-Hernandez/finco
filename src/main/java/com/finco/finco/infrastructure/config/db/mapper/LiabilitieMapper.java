package com.finco.finco.infrastructure.config.db.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.liabilitie.model.Liabilitie;
import com.finco.finco.infrastructure.config.db.schema.LiabilitieSchema;

@Component
public class LiabilitieMapper {

    private final UserMapper userMapper;

    public LiabilitieMapper(@Lazy UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Liabilitie toLiabilitie(LiabilitieSchema liabilitieSchema) {
        if (liabilitieSchema == null) {
            return null;
        }

        Liabilitie liabilitie = new Liabilitie();

        liabilitie.setId(liabilitieSchema.getId());
        liabilitie.setUser(userMapper.toLigthUser(liabilitieSchema.getUser()));
        liabilitie.setName(liabilitieSchema.getName());
        liabilitie.setPendingBalance(liabilitieSchema.getPendingBalance());
        liabilitie.setInterestRate(liabilitieSchema.getInterestRate());
        liabilitie.setStartDate(liabilitieSchema.getStartDate());
        liabilitie.setEndDate(liabilitieSchema.getEndDate());
        liabilitie.setDescription(liabilitieSchema.getDescription());

        return liabilitie;
    }

    public LiabilitieSchema toLiabilitieSchema(Liabilitie liabilitie) {
        if (liabilitie == null) {
            return null;
        }

        if (liabilitie.getUser() == null || liabilitie.getUser().getId() == null) {
            throw new IllegalArgumentException("Goal must be associated with an existing User (with an ID).");
        }

        LiabilitieSchema liabilitieSchema = new LiabilitieSchema();

        liabilitieSchema.setId(liabilitie.getId());
        liabilitieSchema.setUser(userMapper.toLightUserSchema(liabilitie.getUser()));
        liabilitieSchema.setName(liabilitie.getName());
        liabilitieSchema.setPendingBalance(liabilitie.getPendingBalance());
        liabilitieSchema.setInterestRate(liabilitie.getInterestRate());
        liabilitieSchema.setStartDate(liabilitie.getStartDate());
        liabilitieSchema.setEndDate(liabilitie.getEndDate());
        liabilitieSchema.setDescription(liabilitie.getDescription());

        return liabilitieSchema;
    }

}
