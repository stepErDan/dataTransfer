package com.eaglesoft.controller.thread;

import java.util.concurrent.*;

/**
 * 线程池初始化类(这是一个单例)
 */
public class ThreadPoolInit {
    public static final ThreadPoolInit instance = new ThreadPoolInit();
    private ThreadPoolExecutor executor;

    //核心线程池数量（即线程池中的线程数目大于这个参数时，提交的任务会被放进任务缓存队列）
    private static final int corePoolSize = 10;
    //最大线程池数量
    private static final int maximumPoolSize = 16;
    //线程存货时间
    private static final long keepAliveTime = 200L;
    //等待时间单位 ： 毫秒
    private static final TimeUnit unit = TimeUnit.MILLISECONDS;
    //任务缓存队列，用来存放等待执行的任务
    //    1）ArrayBlockingQueue：基于数组的先进先出队列，此队列创建时必须指定大小；
    //　　2）LinkedBlockingQueue：基于链表的先进先出队列，如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE；
    //　　3）synchronousQueue：这个队列比较特殊，它不会保存提交的任务，而是将直接新建一个线程来执行新来的任务。
    private static final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue(5);

    public ThreadPoolInit(){
        //如果线程池对象是空的，创建线程池对象
        if(null == executor){
            //创建线程池对象
            executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
            executor.allowCoreThreadTimeOut(true);
            try {
                //AbortPolicy() : 丢弃任务并抛出RejectedExecutionException异常
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
            }catch (RejectedExecutionException e){
                //todo 异常处理
            }
            //默认情况下，创建线程池之后，线程池中是没有线程的，需要提交任务之后才会创建线程。
            //初始化一个线程
            executor.prestartCoreThread();
        }
    }

    public ExecutorService getExecutor(){
        return this.executor;
    }

    //可设置最大核心线程池
    public void setCorePoolSize(int corePoolSize){
        this.executor.setCorePoolSize(corePoolSize);
    }

    //可设置最大可容纳线程池数量
    public void setMaximumPoolSize(int maximumPoolSize){
        this.executor.setCorePoolSize(maximumPoolSize);
    }
}
