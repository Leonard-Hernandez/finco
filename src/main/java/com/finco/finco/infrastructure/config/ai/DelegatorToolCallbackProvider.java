package com.finco.finco.infrastructure.config.ai;

import java.util.stream.Stream;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;

public class DelegatorToolCallbackProvider implements ToolCallbackProvider {

    private final ToolCallbackProvider delegate;

    public DelegatorToolCallbackProvider(ToolCallbackProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public ToolCallback[] getToolCallbacks() {
        return Stream.of(this.delegate.getToolCallbacks())
			.map(callback -> new DelegatorToolCallback(callback))
			.toArray(ToolCallback[]::new);
    }

}
