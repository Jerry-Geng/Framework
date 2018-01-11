package me.jerry.framework.comm;

import java.util.Map;

/**返回数据解析器
 * @author JerryGeng
 * @param 解析的目标数据类型
 */
public interface IRespDataResolver<T> {
	/**
	 * 解析方法
	 * @param body 返回数据体
	 * @param header 返回头信息
	 * @return 解析后的目标对象实体
	 */
    public T resolve(byte[] body, Map<String, Object> header);
}
