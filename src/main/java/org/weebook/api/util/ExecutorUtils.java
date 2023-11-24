package org.weebook.api.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorUtils {
    public static Executor executor = Executors.newCachedThreadPool();
}
