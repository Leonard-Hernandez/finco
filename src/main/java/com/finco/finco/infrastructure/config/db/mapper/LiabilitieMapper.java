package com.finco.finco.infrastructure.config.db.mapper;

import org.springframework.stereotype.Component;

import com.finco.finco.entity.liabilitie.model.Liabilitie;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.LiabilitieSchema;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

@Component
public class LiabilitieMapper {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public LiabilitieMapper(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
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

        UserSchema userSchema = null;
        User userDomain = liabilitie.getUser();

        if (userDomain == null || userDomain.getId() == null) {
            throw new IllegalArgumentException("Goal must be associated with an existing User (with an ID).");
        }

        userSchema = userRepository.findById(userDomain.getId())
                .orElseThrow(() -> new RuntimeException("user not found"));

        LiabilitieSchema liabilitieSchema = new LiabilitieSchema();

        liabilitieSchema.setId(liabilitie.getId());
        liabilitieSchema.setUser(userSchema);
        liabilitieSchema.setName(liabilitie.getName());
        liabilitieSchema.setPendingBalance(liabilitie.getPendingBalance());
        liabilitieSchema.setInterestRate(liabilitie.getInterestRate());
        liabilitieSchema.setStartDate(liabilitie.getStartDate());
        liabilitieSchema.setEndDate(liabilitie.getEndDate());
        liabilitieSchema.setDescription(liabilitie.getDescription());

        return liabilitieSchema;
    }

}
