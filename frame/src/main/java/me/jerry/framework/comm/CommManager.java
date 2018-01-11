package me.jerry.framework.comm;

import android.os.Handler;
import android.os.HandlerThread;


/**通讯管理器，管理所有通信线程，统筹通讯逻辑
 * @author JerryGeng
 */
public class CommManager {
	/**
	 * 单例
	 */
    public static CommManager commManager;

    private CommManager(){};
    /**
     * 获取单例
     * @return
     */
    public static CommManager getInstance() {
        if(commManager == null) {
            commManager = new CommManager();
        }
        return commManager;
    }
    /**
     * 通讯实体队列
     */
    private CommunicationQuene communicationQuene = new CommunicationQuene();
    /**
     * 通讯线程数组
     */
    private CommThread[] threads;
    /**
     * 通讯事件抛出到达的线程
     */
    private HandlerThread netThread;
    /**
     * 是否初始化完成
     */
    private boolean inited = false;
    /**
     * 初始化通讯管理器，需手动调用
     * @param threadNum 通讯线程数
     * @param netProcess 指定的全局网络通信进程实体，默认为{@link DefaultHttpProcess}
     * @param eventListener 指定的全局事件监听器
     * @param connectTimeout 全局连接超时时间
     * @param requestTimeout 全局请求超时时间
     * @param retryTimes 全局重试次数
     * @param cacheable 是否开启全局缓存模式
     */
    public void init(int threadNum, INetProcess netProcess, ICommEventListener eventListener, int connectTimeout, int requestTimeout, int retryTimes, boolean cacheable) {
        if(!inited) {
            inited = true;
            netThread = new HandlerThread("netProcessCallback");
            netThread.start();
            Handler handler = new Handler(netThread.getLooper());
            communicationQuene = new CommunicationQuene();
            threads = new CommThread[threadNum];
            if(netProcess == null) netProcess = new DefaultHttpProcess();
                for(int i = 0; i < threadNum; i ++) {
                threads[i] = new CommThread(communicationQuene, netProcess, eventListener, handler);
                threads[i].setGlobalConnectTimeout(connectTimeout);
                threads[i].setGlobalReadTimeout(requestTimeout);
                threads[i].setGlobalRetryTimes(retryTimes);
                threads[i].setGlobalCacheable(cacheable);
                threads[i].start();
            }
        }
    }

    /**
     * 初始化通讯管理器，需手动调用，默认全局连接超时时间30s,全局请求超时时间30s,全局重试次数3次，不开启全局缓存
     * @param threadNum 通讯线程数
     * @param netProcess 指定的全局网络通信进程实体，默认为{@link DefaultHttpProcess}
     * @param eventListener 指定的全局事件监听器
     */
    public void init(int threadNum, INetProcess netProcess, ICommEventListener eventListener) {
        if(!inited) {
            inited = true;
            netThread = new HandlerThread("netProcessCallback");
            netThread.start();
            Handler handler = new Handler(netThread.getLooper());
            communicationQuene = new CommunicationQuene();
            threads = new CommThread[threadNum];
            if(netProcess == null) netProcess = new DefaultHttpProcess();
            for(int i = 0; i < threadNum; i ++) {
                threads[i] = new CommThread(communicationQuene, netProcess, eventListener, handler);
                threads[i].setGlobalConnectTimeout(30 * 1000);
                threads[i].setGlobalReadTimeout(30 * 1000);
                threads[i].setGlobalRetryTimes(3);
                threads[i].setGlobalCacheable(false);
                threads[i].start();
            }
        }
    }
    /**
     * 发送一条数据请求
     * @param commEntity 通讯实体
     */
    public synchronized void sendMessage(CommEntity commEntity) {
        communicationQuene.addCommEntity(commEntity);
    }
    /**
     * 取消所有数据请求，当前正在进行请求无法取消
     */
    public synchronized void cancelAll() {
        communicationQuene.clearQueue();
    }
    /**
     * 取消某一条数据请求，当前正在进行的请求无法取消
     * @param commEntity 待取消的通讯实体
     */
    public synchronized void cancel(CommEntity commEntity) {
        communicationQuene.delete(commEntity);
    }
    /**
     * 清空缓存
     */
    public synchronized void clearCache() {
        communicationQuene.clearCache();
    }
    /**
     * 关闭通讯管理器，释放所有资源
     */
    public void release() {
        for(CommThread thread : threads) {
            thread.cancel();
        }
    }

}
