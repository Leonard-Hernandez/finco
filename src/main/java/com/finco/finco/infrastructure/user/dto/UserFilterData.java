package com.finco.finco.infrastructure.user.dto;

import com.finco.finco.entity.pagination.filter.IUserFilterData;

public record UserFilterData(Long userId, String name, String email, Boolean enable) implements IUserFilterData {

}
