package io.aa.common;

import io.aa.common.util.BlockingTaskExecutor;

public final class CommonPools {

    private static final BlockingTaskExecutor BLOCKING_TASK_EXECUTOR = BlockingTaskExecutor.of();

    public static BlockingTaskExecutor blockingTaskExecutor() {
        return BLOCKING_TASK_EXECUTOR;
    }

    private CommonPools() {}
}
