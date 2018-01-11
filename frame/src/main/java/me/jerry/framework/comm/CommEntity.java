package me.jerry.framework.comm;

import java.io.Serializable;
import java.util.Map;

/**通讯实体类
 * @author JerryGeng
 */
public class CommEntity implements Serializable, Cloneable {
	/**
	 * 请求数据模型
	 */
    private RequestBean requestBean;
    /**
     * 响应数据模型
     */
    private ResponseBean responseBean;
    /**
     * 请求方法类型
     */
    private ERequestType requestType;
    /**
     * 在使用全局监听器的时候用来标记是哪一条请求的响应
     */
    private Object communicateTag;
    /**
     * 请求超时时间
     */
    private int requestTimeout = 0;
    /**
     * 连接超时时间
     */
    private int connectTimeout = 0;
    /**
     * 重试次数
     */
    private int retryTimes = 0;
    /**
     * 是否开启缓存模式
     */
    private Boolean cacheable = null;
    /**
     * 网络通信进程实体
     */
    private INetProcess netProcess;
    /**
     * 事件监听器
     */
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
    /**
     * 请求数据模型
     * @author JerryGeng
     *
     */
    public static class RequestBean {
    	/**
    	 * 请求地址
    	 */
        public String url;
        /**
         * 请求参数
         */
        public Map<String, Object> params;
        /**
         * 头信息
         */
        public Map<String, Object> headers;
        /**
         * 如果请求参数，请求地址，头信息三要素相同，则表示是请求数据相同
         */
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
    /**
     * 返回数据模型
     * @author JerryGeng
     * @param <T> 执行自动解析转换的目标数据类型
     */
    public static class ResponseBean<T> {
    	/**
    	 * 返回数据体
    	 */
        public byte[] body;
        /**
         * 返回数据头信息
         */
        public Map<String, Object> headers;
        /**
         * 自动解析器实体
         */
        public IRespDataResolver<T> resolver;
    }
    /**
     * 数据请求方法
     * @author JerryGeng
     *
     */
    public static enum ERequestType {
    	/**
    	 * POST请求
    	 */
        REQUEST_TYPE_POST,
    	/**
    	 * GET请求
    	 */
        REQUEST_TYPE_GET,
    	/**
    	 * PUT请求
    	 */
        REQUEST_TYPE_PUT,
    	/**
    	 * DELETE请求
    	 */
        REQUEST_TYPE_DELETE,
    	/**
    	 * HEAD请求
    	 */
        REQUEST_TYPE_HEAD,
    	/**
    	 * OPTIONS请求
    	 */
        REQUEST_TYPE_OPTIONS,
    	/**
    	 * PATCH请求
    	 */
        REQUEST_TYPE_PATCH,
    	/**
    	 * TRACE请求
    	 */
        REQUEST_TYPE_TRACE;
    }
    /**
     * 请求数据和请求数据类型相同，则表示通信实体相同
     */
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
