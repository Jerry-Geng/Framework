package me.jerry.framework.comm;

import java.util.Map;

/**
 * T: the target result type
 * Created by Jerry on 2017/8/7.
 */

public interface IRespDataResolver<T> {
    public T resolve(byte[] body, Map<String, Object> header);
}
