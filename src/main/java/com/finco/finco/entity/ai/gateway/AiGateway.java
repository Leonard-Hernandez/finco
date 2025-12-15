package com.finco.finco.entity.ai.gateway;

public interface AiGateway {

    String getAnswer(String question, Long userId, byte[] image, String imageExtension);

}
