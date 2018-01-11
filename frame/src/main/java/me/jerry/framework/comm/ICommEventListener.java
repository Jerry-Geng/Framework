package me.jerry.framework.comm;

/**通讯事件监听器
 * @author JerryGeng
 */
public interface ICommEventListener {
	/**
	 * 通讯请求开始
	 * @param commEntity
	 */
    public void onStart(CommEntity commEntity);
    /**
     * 通讯请求结束
     * @param commEntity
     */
    public void onEnd(CommEntity commEntity);
    /**
     * 通信出错
     * @param e 包含错误信息的异常类
     * @param commEntity
     */
    public void onError(NetException e, CommEntity commEntity);
}
