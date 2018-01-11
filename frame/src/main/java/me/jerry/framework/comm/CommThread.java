package me.jerry.framework.comm;


import android.os.Handler;
import android.util.Log;

/**通讯线程
 * @author JerryGeng
 */
public class CommThread extends Thread {
	/**
	 * 线程已取消标记
	 */
    private boolean canceled = false;
    /**
     * 通讯实体队列
     */
    public CommunicationQuene communicationQuene;
    /**
     * 全局网络通信进程实体
     */
    private INetProcess netProcess;
    /**
     * 全局事件监听器
     */
    private ICommEventListener commEventListener;
    /**
     * 用于抛出事件的handler，绑定的线程是{@link CommManager#netThread }
     */
    private Handler handler;
    /**
     * 全局重试次数
     */
    private int globalRetryTimes = 0;
    /**
     * 全局连接超时时间
     */
    private int globalConnectTimeout = 0;
    /**
     * 全局请求超时时间
     */
    private int globalReadTimeout = 0;
    /**
     * 全局缓存开启标记
     */
    private boolean globalCacheable = false;
    /**
     * 
     * @param communicationQuene 通讯实体队列
     * @param netProcess 指定的全局网络通信进程实体
     * @param commEventListener 指定的全局事件监听器
     * @param handler 用于抛出事件的handler
     */
    public CommThread(CommunicationQuene communicationQuene, INetProcess netProcess, ICommEventListener commEventListener, Handler handler) {
        this.communicationQuene = communicationQuene;
        this.netProcess = netProcess;
        this.commEventListener = commEventListener;
        this.handler = handler;
    }

    public void setGlobalRetryTimes(int globalRetryTimes) {
        this.globalRetryTimes = globalRetryTimes;
    }

    public void setGlobalConnectTimeout(int globalConnectTimeout) {
        this.globalConnectTimeout = globalConnectTimeout;
    }

    public void setGlobalReadTimeout(int globalReadTimeout) {
        this.globalReadTimeout = globalReadTimeout;
    }

    public void setGlobalCacheable(boolean globalCacheable) {
        this.globalCacheable = globalCacheable;
    }

    public void cancel() {
        canceled = true;
    }
    /**
     * 从队列中读取通讯实体并发起通讯请求
     * <ul>
     * <li>所有请求参数以通讯实体中定义的优先，通讯实体中未定义的则采用全局设置</li>
     * <li>请求结束自动存储缓存信息，执行超时重发等，或者将这些处理交给INetProcess</li>
     * </ul>
     */
    @Override
    public void run() {
a:        while(!canceled) {
            // get CommEntity and communicate.
            final CommEntity commEntity = communicationQuene.getCommEntity();
            if(commEntity == null) {
                // no communication
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            if(globalConnectTimeout > 0 && commEntity.getConnectTimeout() <= 0) {
                commEntity.setConnectTimeout(globalConnectTimeout);
            }
            if(globalReadTimeout > 0 && commEntity.getRequestTimeout() <= 0) {
                commEntity.setRequestTimeout(globalReadTimeout);
            }
            if(globalRetryTimes > 0 && commEntity.getRetryTimes() <= 0) {
                commEntity.setRetryTimes(globalRetryTimes);
            }
            if(commEntity.isCacheable() == null) {
                commEntity.setCacheable(globalCacheable);
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(commEntity.getEventListener() != null) {
                        commEntity.getEventListener().onStart(commEntity);
                    }
                    if(commEventListener != null) {
                        commEventListener.onStart(commEntity);
                    }
                }
            });
            Log.i("net process", "cache checked");
            INetProcess np = null;
            if(commEntity.getNetProcess() == null) {
                if(netProcess == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(commEntity.getEventListener() != null) {
                                commEntity.getEventListener().onError(new NetException("no net process! we can not handle the communication entity!"), commEntity);
                            }
                            if(commEventListener != null) {
                                commEventListener.onError(new NetException("no net process! we can not handle the communication entity!"), commEntity);
                            }
                        }
                    });
                    continue;
                } else {
                    np = this.netProcess;
                }
            } else {
                np = commEntity.getNetProcess();
            }
            Log.i("net process", "got netProcess");
            // check cacheable, if true, get response value from cache queue.
            if(commEntity.isCacheable() && !np.innerCache()) {
                final CommEntity ce;
                if((ce = communicationQuene.isInCache(commEntity)) != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(commEntity.getEventListener() != null) {
                                commEntity.getEventListener().onEnd(ce);
                            }
                            if(commEventListener != null) {
                                commEventListener.onEnd(ce);
                            }
                        }
                    });
                    continue;
                }
            }
            int retryTimes = commEntity.getRetryTimes();
            if(np.innerRetry()) {
                retryTimes = 1;
            }
            while(retryTimes-- > 0) {
                try {
                    byte[] data = np.process(commEntity);
                    if(commEntity.getResponseBean() == null) {
                        commEntity.setRequestBean(new CommEntity.RequestBean());
                    }
                    commEntity.getResponseBean().body = data;
                    break;
                } catch (final NetException e) {
                    // if timeout and can retry, retry, else throw and handle it.
                    if(e.getErrCode() == NetException.ERR_CODE_TIMEOUT && retryTimes > 0) {
                        // timeout
                        continue;
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(commEntity.getEventListener() != null) {
                                    commEntity.getEventListener().onError(e, commEntity);
                                }
                                if(commEventListener != null) {
                                    commEventListener.onError(e, commEntity);
                                }
                            }
                        });
                        continue a;
                    }
                }
            }
            Log.i("net process", "process end");
            // communicate complicate
            if(commEntity.isCacheable() && !np.innerCache()) {
                communicationQuene.putInCache(commEntity);
                Log.i("net process", "cache ok");
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(commEntity.getEventListener() != null) {
                        commEntity.getEventListener().onEnd(commEntity);
                    }
                    if(commEventListener != null) {
                        commEventListener.onEnd(commEntity);
                    }
                }
            });
        }
    }
}
