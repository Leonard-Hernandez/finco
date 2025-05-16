package com.finco.finco.entity.liabilitie.gateway;

import java.util.Optional;

import com.finco.finco.entity.liabilitie.model.Liabilitie;

public interface LiabilitieGateway {

    Liabilitie create(Liabilitie liabilitie);
    Liabilitie update(Liabilitie liabilitie);
    void delete(Liabilitie liabilitie);

    Optional<Liabilitie> findById(Long id);

}
