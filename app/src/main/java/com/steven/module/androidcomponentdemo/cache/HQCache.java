package com.steven.module.androidcomponentdemo.cache;

public class HQCache {
    /**
     * 获取cache实例
     *
     * @param cacheName 缓存名称
     * @return
     */
    public static HQCache getCache(String cacheName) {
        return null;
    }

    /**
     * 是否包含某个缓存
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return false;
    }

    /**
     * 保存String数据到缓存
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {

    }

    /**
     * 保存String数据到缓存
     *
     * @param key
     * @param value
     * @param expireSeconds 过期时间，单位：秒
     */
    public void put(String key, String value, int expireSeconds) {

    }

    /**
     * 读取String缓存
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return "";
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public void remove(String key) {

    }

    /**
     * 清除所有缓存
     */
    public void removeAll() {

    }
}
