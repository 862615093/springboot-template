package com.ww.template.utils;

import cn.hutool.core.thread.ThreadFactoryBuilder;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static com.ww.template.constant.BaseConstant.CACHE_THREAD_PREFIX_NAME;
import static com.ww.template.constant.BaseConstant.THREAD_KEEP_ALIVE_TIME;


/**
 * 线程池管理工具类
 *
 * @author weiwang127
 */
public class ThreadPoolUtils {

    /**
     * 初始化核心线程池大小
     */
    public static final Integer CACHE_THREAD_NUM = Runtime.getRuntime().availableProcessors() + 1;

    @Resource
    public enum ThreadEnum {
        FixedThread,
        CachedThread,
        SingleThread
    }

    private static ExecutorService exec;

    private ThreadPoolUtils() {
        throw new RuntimeException("can't instantiate ...");
    }

    /**
     * ThreadPoolUtils构造函数
     *
     * @param type 线程池类型
     */
    public ThreadPoolUtils(final ThreadEnum type) {
        if(exec==null) {
            switch (type) {
                case FixedThread:
                    //固定线程数目的线程池
                    exec = new ThreadPoolExecutor(CACHE_THREAD_NUM, CACHE_THREAD_NUM,
                            THREAD_KEEP_ALIVE_TIME,
                            TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<>(),
                            new ThreadFactoryBuilder().setNamePrefix(CACHE_THREAD_PREFIX_NAME).build());
                    break;
                case SingleThread:
                    //一个线程的线程池
                    exec = Executors.newSingleThreadExecutor();
                    break;
                case CachedThread:
                    //构造一个缓冲功能的线程池
                    exec = Executors.newCachedThreadPool();
                    break;
                default:
                    throw new RuntimeException("ThreadEnum can't null ...");
            }
        }
    }

    /**
     * 执行给定的命令
     * @param command 命令
     */
    public void execute(final Runnable command) {
        exec.execute(command);
    }

    /**
     * 执行给定的命令链表
     * @param commands 命令链表
     */
    public void execute(final List<Runnable> commands) {
        for (Runnable command : commands) {
            exec.execute(command);
        }
    }

    /**
     * 待以前提交的任务执行完毕后关闭线程池
     */
    public void shutDown() {
        exec.shutdown();
    }

    /**
     * 试图停止所有正在执行的活动任务
     *
     * @return 等待执行的任务的列表
     */
    public List<Runnable> shutDownNow() {
        return exec.shutdownNow();
    }

    /**
     * 判断线程池是否已关闭
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public boolean isShutDown() {
        return exec.isShutdown();
    }

    /**
     * 关闭线程池后判断所有任务是否都已完成
     * 注意，除非首先调用 shutdown 或 shutdownNow，否则 isTerminated 永不为 true。
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public boolean isTerminated() {
        return exec.isTerminated();
    }


    /**
     * 请求关闭、发生超时或者当前线程中断
     *
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @return {@code true}: 请求成功<br>{@code false}: 请求超时
     * @throws InterruptedException 终端异常
     */
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return exec.awaitTermination(timeout, unit);
    }

    /**
     * 提交一个Callable任务用于执行
     *
     * @param task 任务
     * @param <T>  泛型
     * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回该任务的结果。
     */
    public <T> Future<T> submit(final Callable<T> task) {
        return exec.submit(task);
    }

    /**
     * 提交一个Runnable任务用于执行
     *
     * @param task   任务
     * @param result 返回的结果
     * @param <T>    泛型
     * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回该任务的结果。
     */
    public <T> Future<T> submit(final Runnable task, final T result) {
        return exec.submit(task, result);
    }

    /**
     * 提交一个Runnable任务用于执行
     *
     * @param task 任务
     * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回null结果。
     */
    public Future<?> submit(final Runnable task) {
        return exec.submit(task);
    }

    /**
     * 执行给定的任务
     *
     * @param tasks 任务集合
     * @param <T>   泛型
     * @return 表示任务的 Future 列表，列表顺序与给定任务列表的迭代器所生成的顺序相同，每个任务都已完成。
     * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务。
     */
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return exec.invokeAll(tasks);
    }

    /**
     * 执行给定的任务
     *
     * @param tasks   任务集合
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @param <T>     泛型
     * @return 表示任务的 Future 列表，列表顺序与给定任务列表的迭代器所生成的顺序相同。如果操作未超时，则已完成所有任务。如果确实超时了，则某些任务尚未完成。
     * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务
     */
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws
            InterruptedException {
        return exec.invokeAll(tasks, timeout, unit);
    }
}