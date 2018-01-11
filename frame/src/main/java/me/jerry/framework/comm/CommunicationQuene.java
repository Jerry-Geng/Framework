package me.jerry.framework.comm;

import java.util.ArrayList;
import java.util.List;

/**通讯实体队列
 * @author JerryGeng
 */

public class CommunicationQuene {
	/**
	 * 队列实体数据
	 */
    private List<CommEntity> commEntities = new ArrayList<>();
    /**
     * 缓存数据
     */
    private List<CommEntity> cacheQueue = new ArrayList<>();
    /**
     * 添加实体到队列
     * @param commEntity
     */
    public synchronized void addCommEntity(CommEntity commEntity) {
        synchronized (commEntities) {
            commEntities.add(commEntity);
        }
    }
    /**
     * 清空队列
     */
    public synchronized void clearQueue() {
        synchronized (commEntities) {
            commEntities.clear();
        }
    }
    /**
     * 删除指定实体
     * @param commEntity
     */
    public synchronized void delete(CommEntity commEntity) {
        synchronized (commEntities) {
            commEntities.remove(commEntity);
        }
    }
    /**
     * 清空缓存
     */
    public synchronized void clearCache() {
        synchronized (cacheQueue) {
            cacheQueue.clear();
        }
    }
    /**
     * 取出队列中一条数据
     * @return
     */
    public synchronized CommEntity getCommEntity() {
        synchronized (commEntities) {
            if(commEntities.isEmpty()) return null;
            CommEntity commEntity = commEntities.get(0);
            commEntities.remove(0);
            return commEntity;
        }
    }
    /**
     * 将实体加入缓存
     * @param commEntity
     */
    public synchronized void putInCache(CommEntity commEntity) {
        synchronized (cacheQueue) {
            if(commEntity != null) {
                CommEntity original = null;
                for(CommEntity cache : cacheQueue) {
                    if(cache.equals(commEntity)) {
                        original = cache;
                        break;
                    }
                }
                if(original != null) {
                    cacheQueue.remove(original);
                }
                cacheQueue.add(commEntity);
            }
        }
    }
    /**
     * 判断缓存中是否存在某网络通讯实体
     * @param commEntity
     * @return 存在则拿出缓存中的克隆对象（其中已包含所需的返回对象实体），不存在则返回null
     */
    public synchronized CommEntity isInCache(CommEntity commEntity) {
        synchronized (cacheQueue) {
            for(CommEntity cache : cacheQueue) {
                if(cache.equals(commEntity)) {
                    return cache.clone();
                }
            }
            return null;
        }
    }

}
