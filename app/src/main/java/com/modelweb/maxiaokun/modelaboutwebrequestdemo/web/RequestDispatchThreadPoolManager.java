package com.modelweb.maxiaokun.modelaboutwebrequestdemo.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RequestDispatchThreadPoolManager
 *
 * @author Mazoh  Ma
 * @version 2.4.5
 * @Description
 * @createDate 2016年3月7日
 */

public class RequestDispatchThreadPoolManager {
    public static final String tag = "RequestDispatchThreadPoolManager";

    private volatile static RequestDispatchThreadPoolManager requestDispatchThreadPoolManager;
    //根据java虚拟机可用的处理器计算线程池的大小
    private static final int DEFAULTTHREADSIZE = Runtime.getRuntime().availableProcessors() * 3 + 2;
    private static final int FIXEDTHREADPOOLTYPE = 0;
    private static final int CACHEDTHREADPOOLTYPE = 1;
    private static final int SCHEDULEDTHREADPOOLTYPE = 2;
    private static final int SINGLETHREADEXECUTORTYPE = 3;


    private int poolSize = 0;//指定线程池线程个数
    private int type = -1;//线程池类型
    private ExecutorService executorService;

    /**
     * @return 获取线程池大小
     */
    public int getPoolSize() {
        return poolSize;
    }

    public RequestDispatchThreadPoolManager setPoolSize(int poolSize) {
        this.poolSize = poolSize;
        return this ;
    }

    /**
     * @return 获取线程池名字
     */
    public String getExecutorServiceName() {
        if (executorService == null) {
            return null;
        }
        return executorService.getClass().getSimpleName();
    }

    /**
     * 0 为指定线程数newFixedThreadPool线程池
     * 1 为newCachedThreadPool缓存线程池
     * 2 newScheduledThreadPool 定时线程池
     * 3 newSingleThreadExecutor 单例线程池
     *
     * @return 获取线程池类型
     */
    public int getType() {
        return type;
    }

    public RequestDispatchThreadPoolManager setType(int type) {
        this.type = type ;
        return this;
    }

    private RequestDispatchThreadPoolManager() {
        if (executorService == null || executorService.isShutdown()) {
        }
    }

    private RequestDispatchThreadPoolManager(int type, int poolSize) {
        if (executorService == null || executorService.isShutdown()) {
            this.poolSize = poolSize;
            setType(type);
        }
    }

    /**
     * 判断线程池是否关闭
     *
     * @return
     */
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    /**
     * 获取当前线程池实例
     *
     * @return
     */
    //获取当前设置的线程池
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * 很据业务选择初始化不同类型的线程池
     * 默认为可定长度的线程池
     *
     * @param threadPoolType 0 为指定线程数newFixedThreadPool线程池
     *                       1 为newCachedThreadPool缓存线程池
     *                       2 newScheduledThreadPool 定时线程池
     *                       3 newSingleThreadExecutor 单例线程池
     *                       default 为指定线程数newFixedThreadPool线程池 个数为cpu个数*3 + 2
     */
    public RequestDispatchThreadPoolManager initThreadPoolType(int threadPoolType) {
        if (executorService != null) {
            return null;
        }
        if( this.type == 0){
            this.type = threadPoolType;
        }
        switch (type) {
            case CACHEDTHREADPOOLTYPE:
                executorService = Executors.newCachedThreadPool();
                break;

            case SCHEDULEDTHREADPOOLTYPE:
                executorService = Executors.newScheduledThreadPool(poolSize != 0 ? poolSize : DEFAULTTHREADSIZE);
                break;

            case SINGLETHREADEXECUTORTYPE:
                executorService = Executors.newSingleThreadExecutor();
                break;

            case FIXEDTHREADPOOLTYPE:

            default:
                executorService = Executors.newFixedThreadPool(poolSize != 0 ? poolSize : DEFAULTTHREADSIZE);
                break;
        }
        return this;
    }
    /**
     * 很据业务选择初始化不同类型的线程池
     * 默认为可定长度的线程池
     *
     * @param type inside the method
     *                       0 为指定线程数newFixedThreadPool线程池
     *                       1 为newCachedThreadPool缓存线程池
     *                       2 newScheduledThreadPool 定时线程池
     *                       3 newSingleThreadExecutor 单例线程池
     *                       default 为指定线程数newFixedThreadPool线程池 个数为cpu个数*3 + 2
     */
    public RequestDispatchThreadPoolManager initThreadPoolType() {
        if (executorService != null) {
            return null;
        }

        switch (type) {
            case CACHEDTHREADPOOLTYPE:
                executorService = Executors.newCachedThreadPool();
                break;

            case SCHEDULEDTHREADPOOLTYPE:
                executorService = Executors.newScheduledThreadPool(poolSize != 0 ? poolSize : DEFAULTTHREADSIZE);
                break;

            case SINGLETHREADEXECUTORTYPE:
                executorService = Executors.newSingleThreadExecutor();
                break;

            case FIXEDTHREADPOOLTYPE:

            default:
                executorService = Executors.newFixedThreadPool(poolSize != 0 ? poolSize : DEFAULTTHREADSIZE);
                break;
        }
        return this;
    }
    /**
     * 启动默认线程池的网络管理请求类,这里可以不用双重校验锁
     *
     * @return
     */
    public static RequestDispatchThreadPoolManager getInstance() {
        if (requestDispatchThreadPoolManager == null) {
            requestDispatchThreadPoolManager = new RequestDispatchThreadPoolManager();
        }
        return requestDispatchThreadPoolManager;
    }

    /**
     * 销毁回收线程池，在application的onTerminate里面调用
     */
    public void recycleThreadPool() {
        if (executorService == null || executorService.isShutdown()) {
            return;
        }
        if (!executorService.isShutdown()) {
            executorService.shutdown();
            executorService = null;
        }
    }


}
