package io.aa.common.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public final class BlockingTaskExecutor extends ScheduledThreadPoolExecutor {

    private static final int DEFAULT_CORE_POOL_SIZE = 200;

    private BlockingTaskExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public static BlockingTaskExecutor of() {
        return new BlockingTaskExecutor(DEFAULT_CORE_POOL_SIZE);
    }
}
