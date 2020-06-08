import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * cmd工厂类
 *
 * @author panan
 * @since 2019-12-09
 */
public class CommThreadFactory implements ThreadFactory {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    private final ThreadGroup group;

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final String namePrefix;

    /**
     * cmd工厂方法
     *
     * @param threadName 线程name
     */
    public CommThreadFactory(String threadName) {
        SecurityManager securityManager = System.getSecurityManager();
        group = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        String poolName = "pool-";
        if (threadName != null && !threadName.trim().isEmpty()) {
            poolName = poolName + threadName + "-";
        }
        namePrefix = poolName + POOL_NUMBER.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(group, runnable, namePrefix + threadNumber.getAndIncrement(), 0);
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        return thread;
    }
}
