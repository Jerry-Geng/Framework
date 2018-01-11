package me.jerry.framework.comm;

import android.os.Handler;
import android.os.HandlerThread;


/**
 * Created by Jerry on 2017/8/7.
 */

public class CommManager {

    public static CommManager commManager;

    private CommManager(){};

    public static CommManager getInstance() {
        if(commManager == null) {
            commManager = new CommManager();
        }
        return commManager;
    }

    private CommunicationQuene communicationQuene = new CommunicationQuene();

    private CommThread[] threads;
    private HandlerThread netThread;
    private boolean inited = false;

    public void init(int threadNum, INetProcess netProcess, ICommEventListener endListener, int connectTimeout, int requestTimeout, int retryTimes, boolean cacheable) {
        if(!inited) {
            inited = true;
            netThread = new HandlerThread("netProcessCallback");
            netThread.start();
            Handler handler = new Handler(netThread.getLooper());
            communicationQuene = new CommunicationQuene();
            threads = new CommThread[threadNum];
            if(netProcess == null) netProcess = new DefaultHttpProcess();
                for(int i = 0; i < threadNum; i ++) {
                threads[i] = new CommThread(communicationQuene, netProcess, endListener, handler);
                threads[i].setGlobalConnectTimeout(connectTimeout);
                threads[i].setGlobalReadTimeout(requestTimeout);
                threads[i].setGlobalRetryTimes(retryTimes);
                threads[i].setGlobalCacheable(cacheable);
                threads[i].start();
            }
        }
    }


    public void init(int threadNum, INetProcess netProcess, ICommEventListener endListener) {
        if(!inited) {
            inited = true;
            netThread = new HandlerThread("netProcessCallback");
            netThread.start();
            Handler handler = new Handler(netThread.getLooper());
            communicationQuene = new CommunicationQuene();
            threads = new CommThread[threadNum];
            if(netProcess == null) netProcess = new DefaultHttpProcess();
            for(int i = 0; i < threadNum; i ++) {
                threads[i] = new CommThread(communicationQuene, netProcess, endListener, handler);
                threads[i].setGlobalConnectTimeout(30 * 1000);
                threads[i].setGlobalReadTimeout(30 * 1000);
                threads[i].setGlobalRetryTimes(3);
                threads[i].setGlobalCacheable(false);
                threads[i].start();
            }
        }
    }

    public synchronized void sendMessage(CommEntity commEntity) {
        communicationQuene.addCommEntity(commEntity);
    }

    public synchronized void cancelAll() {
        communicationQuene.clearQueue();
    }

    public synchronized void cancel(CommEntity commEntity) {
        communicationQuene.delete(commEntity);
    }

    public synchronized void clearCache() {
        communicationQuene.clearCache();
    }

    public void release() {
        for(CommThread thread : threads) {
            thread.cancel();
        }
    }

}
