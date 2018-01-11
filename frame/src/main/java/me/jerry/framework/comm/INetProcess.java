package me.jerry.framework.comm;

/**
 * Created by Jerry on 2017/8/7.
 */

public interface INetProcess {
    public byte[] process(CommEntity commEntity) throws NetException;

    public boolean innerRetry();

    public boolean innerCache();
}
