package com.finco.finco.usecase.ai.dto;

public interface IAiAskDto {

    String prompt();
    Long userId();
    String image();
    String imageExtension();

}