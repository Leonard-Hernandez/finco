package com.finco.finco.usecase.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IGoalUpdateData {

    String name();
    BigDecimal targetAmount();
    LocalDate deadLine();
    String description();
    Boolean enable();

}
