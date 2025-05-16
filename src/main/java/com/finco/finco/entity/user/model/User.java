package com.finco.finco.entity.user.model;

import java.time.LocalDateTime;
import java.util.List;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.goal.model.Goal;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime registrationDate;

    private List<Account> accounts;
    private List<Goal> goals;

}
