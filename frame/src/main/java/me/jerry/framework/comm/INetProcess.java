package me.jerry.framework.comm;

/**网络通讯进程
 * @author JerryGeng
 */
public interface INetProcess {
	/**
	 * 进行一次通讯
	 * @param commEntity 通讯实体
	 * @return 接收到的数据
	 * @throws NetException
	 */
    public byte[] process(CommEntity commEntity) throws NetException;
    /**
     * 指定是否在该类中进行内部重试
     * @return
     */
    public boolean innerRetry();
    /**
     * 指定是否在该类中进行内部缓存
     * @return
     */
    public boolean innerCache();
}
