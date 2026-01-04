package com.finco.finco.infrastructure.config.ai;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;

import dev.toonformat.jtoon.JToon;

public class DelegatorToolCallback implements ToolCallback {

    private final ToolCallback delegate;

    public DelegatorToolCallback(ToolCallback delegate) {
        this.delegate = delegate;
    }

    @Override
    public ToolDefinition getToolDefinition() {
        return this.delegate.getToolDefinition();
    }

    @Override
    public String call(String toolInput) {
        String result = this.delegate.call(toolInput);
        return JToon.encodeJson(result);
    }
}
