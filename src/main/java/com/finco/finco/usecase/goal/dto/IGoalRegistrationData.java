package com.finco.finco.usecase.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IGoalRegistrationData {

    Long userId();
    String name();
    BigDecimal targetAmount();
    LocalDate deadLine();
    String description();

}
