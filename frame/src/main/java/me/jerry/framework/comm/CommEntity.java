package me.jerry.framework.comm;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Jerry on 2017/8/7.
 */

public class CommEntity implements Serializable, Cloneable {

    private RequestBean requestBean;

    private ResponseBean responseBean;

    private ERequestType requestType;
    /** to flag which this entity is **/
    private Object communicateTag;

    private int requestTimeout = 0;

    private int connectTimeout = 0;

    private int retryTimes = 0;

    private Boolean cacheable = null;

    private INetProcess netProcess;

    private ICommEventListener eventListener;

    public ICommEventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(ICommEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public Object getCommunicateTag() {
        return communicateTag;
    }

    public void setCommunicateTag(Object communicateTag) {
        this.communicateTag = communicateTag;
    }

    public INetProcess getNetProcess() {
        return netProcess;
    }

    public void setNetProcess(INetProcess netProcess) {
        this.netProcess = netProcess;
    }

    public RequestBean getRequestBean() {
        return requestBean;
    }

    public void setRequestBean(RequestBean requestBean) {
        this.requestBean = requestBean;
    }

    public ResponseBean getResponseBean() {
        return responseBean;
    }

    public void setResponseBean(ResponseBean responseBean) {
        this.responseBean = responseBean;
    }

    public ERequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(ERequestType requestType) {
        this.requestType = requestType;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(Boolean cacheable) {
        this.cacheable = cacheable;
    }

    public static class RequestBean {
        public String url;
        public Map<String, Object> params;
        public Map<String, Object> headers;

        @Override
        public boolean equals(Object obj) {
            if(obj == null || !(obj instanceof RequestBean)) {
                return false;
            }
            RequestBean rb = (RequestBean) obj;
            boolean ret = true;
            ret = (url == rb.url) || (url != null && url.equals(rb.url));
            if(ret == false) return false;
            ret = (params == rb.params) || (params != null && params.equals(rb.params));
            if(ret == false) return false;
            ret = (headers == rb.headers) || (headers != null && headers.equals(rb.headers));
            return ret;
        }
    }

    public static class ResponseBean<T> {
        public byte[] body;
        public Map<String, Object> headers;
        public IRespDataResolver<T> resolver;
    }

    public static enum ERequestType {
        REQUEST_TYPE_POST,
        REQUEST_TYPE_GET,
        REQUEST_TYPE_PUT,
        REQUEST_TYPE_DELETE,
        REQUEST_TYPE_HEAD,
        REQUEST_TYPE_OPTIONS,
        REQUEST_TYPE_PATCH,
        REQUEST_TYPE_TRACE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CommEntity)) {
            return false;
        }
        CommEntity ce = (CommEntity)obj;
        return ((requestBean == ce.requestBean) || (requestBean != null && requestBean.equals(ce.requestBean)))
                && ((requestType == ce.requestType) || (requestType != null && requestType.equals(ce.requestType)));
    }

    @Override
    public CommEntity clone() {
        try {
            return (CommEntity)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
