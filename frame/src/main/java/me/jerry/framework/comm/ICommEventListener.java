package me.jerry.framework.comm;

/**
 * Created by Jerry on 2017/8/7.
 */

public interface ICommEventListener {
    public void onStart(CommEntity commEntity);

    public void onEnd(CommEntity commEntity);

    public void onError(NetException e, CommEntity commEntity);
}
