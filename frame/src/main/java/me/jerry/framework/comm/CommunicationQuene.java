package me.jerry.framework.comm;

import java.util.ArrayList;
import java.util.List;

/**
 * Every communication should be put in the queue.
 * Created by Jerry on 2017/8/7.
 */

public class CommunicationQuene {
    private List<CommEntity> commEntities = new ArrayList<>();

    private List<CommEntity> cacheQueue = new ArrayList<>();

    public synchronized void addCommEntity(CommEntity commEntity) {
        synchronized (commEntities) {
            commEntities.add(commEntity);
        }
    }

    public synchronized void clearQueue() {
        synchronized (commEntities) {
            commEntities.clear();
        }
    }

    public synchronized void delete(CommEntity commEntity) {
        synchronized (commEntities) {
            commEntities.remove(commEntity);
        }
    }

    public synchronized void clearCache() {
        synchronized (cacheQueue) {
            cacheQueue.clear();
        }
    }

    public synchronized CommEntity getCommEntity() {
        synchronized (commEntities) {
            if(commEntities.isEmpty()) return null;
            CommEntity commEntity = commEntities.get(0);
            commEntities.remove(0);
            return commEntity;
        }
    }

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
