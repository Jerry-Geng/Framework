package me.jerry.framework.comm;


import android.os.Handler;
import android.util.Log;

/**
 * Created by Jerry on 2017/8/7.
 */

public class CommThread extends Thread {
    private boolean canceled = false;

    public CommunicationQuene communicationQuene;

    private INetProcess netProcess;

    private ICommEventListener commEventListener;

    private Handler handler;

    private int globalRetryTimes = 0;

    private int globalConnectTimeout = 0;

    private int globalReadTimeout = 0;

    private boolean globalCacheable = false;

    public CommThread(CommunicationQuene communicationQuene, INetProcess netProcess, ICommEventListener commEndListener, Handler handler) {
        this.communicationQuene = communicationQuene;
        this.netProcess = netProcess;
        this.commEventListener = commEndListener;
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
