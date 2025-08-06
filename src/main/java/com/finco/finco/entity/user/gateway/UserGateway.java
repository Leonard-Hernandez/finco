package com.finco.finco.entity.user.gateway;

import java.util.Optional;

import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.pagination.filter.IUserFilterData;
import com.finco.finco.entity.user.model.User;

public interface UserGateway {

    User create(User user);

    User update(User user);

    User delete(User user);

    Optional<User> findById(Long id);

    PagedResult<User> findAll(PageRequest pageRequest);

    PagedResult<User> findAllByFilterData(PageRequest pageRequest, IUserFilterData filterData);

}
