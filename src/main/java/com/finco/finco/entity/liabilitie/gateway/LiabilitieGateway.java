package com.finco.finco.entity.liabilitie.gateway;

import java.util.List;
import java.util.Optional;

import com.finco.finco.entity.liabilitie.model.Liabilitie;
import com.finco.finco.entity.user.model.User;

public interface LiabilitieGateway {

    Liabilitie create(Liabilitie liabilitie);
    Liabilitie update(Liabilitie liabilitie);
    void delete(Liabilitie liabilitie);

    Optional<Liabilitie> findById(Long id);

    List<Liabilitie> findAllByUser(User used);

    List<Liabilitie> findByUserAndNameLike(User user, String search);

}
