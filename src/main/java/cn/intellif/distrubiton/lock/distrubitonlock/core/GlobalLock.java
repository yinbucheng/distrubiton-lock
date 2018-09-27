package cn.intellif.distrubiton.lock.distrubitonlock.core;

import cn.intellif.distrubiton.lock.distrubitonlock.config.ZookeeperConfig;
import cn.intellif.distrubiton.lock.distrubitonlock.utils.ApplicationUtils;
import cn.intellif.distrubiton.lock.distrubitonlock.utils.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public abstract class GlobalLock {
    //存放客户端
    private static ThreadLocal<CuratorFramework> cache = new ThreadLocal<>();
    //存放锁
    private static ThreadLocal<InterProcessMutex> locks = new ThreadLocal<>();
    /**
     * 获取锁
     * @param data
     */
    public static void lock(Object data){
        try {
            CuratorFramework client = CuratorUtils.getClient(ApplicationUtils.getBean(ZookeeperConfig.class).getUrl(), ApplicationUtils.getBean(ZookeeperConfig.class).getNamespace());
            InterProcessMutex lock = new InterProcessMutex(client, "/" + createKey(data));
            locks.set(lock);
            cache.set(client);
            lock.acquire();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * 创建唯一标示
     * @param data
     * @return
     */
    private static String createKey(Object data){
        if(data instanceof String){
            return (String) data;
        }else if(data instanceof Class){
            Class clazz = (Class) data;
            return clazz.getName();
        }
        throw new RuntimeException("current lock only suport Class or String");
    }


    /**
     * 释放锁
     * @param
     */
    public static void unLock(){
        CuratorFramework client = cache.get();
        InterProcessMutex lock = locks.get();
        if(lock!=null){
            try {
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            locks.remove();
        }
        if(client!=null){
            client.close();
            cache.remove();
        }
    }
}
