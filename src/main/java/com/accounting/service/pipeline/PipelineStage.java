package com.accounting.service.pipeline;

public interface PipelineStage<I, O> {
    O process(I input);
} 