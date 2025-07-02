package com.finco.finco.usecase.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IGoalPublicData {

    Long id();
    String name();
    BigDecimal targetAmount();
    LocalDate deadLine();
    String description();
    LocalDateTime creationDate();
    BigDecimal savedAmount();
    boolean enable();

}
