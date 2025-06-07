package com.finco.finco.infrastructure.user.validation.unique;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String>{

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (userRepository != null) {
            Optional<UserSchema> userOptional = userRepository.findByEmail(value);
            return userOptional.isEmpty();
        }
        return true;
    }

}
