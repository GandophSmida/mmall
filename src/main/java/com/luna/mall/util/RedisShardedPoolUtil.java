package com.luna.mall.util;

import com.luna.mall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class RedisShardedPoolUtil {

    /**
     * 设置key的有效期，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedisResource();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("expire key:{} error",key,e);
            return result;
        }finally {
            jedis.close();
        }
        return result;
    }

    //exTime的时间单位是秒
    public static String setEx(String key,String value,int exTime){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedisResource();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error",key,value,e);
            return result;
        }finally {
            jedis.close();
        }
        return result;
    }

    public static String set(String key,String value){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedisResource();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            return result;
        }finally {
            jedis.close();
        }
        return result;
    }

    public static String getSet(String key,String value){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedisResource();
            result = jedis.getSet(key,value);
        } catch (Exception e) {
            log.error("getSet key:{} value:{} error",key,value,e);
            return result;
        }finally {
            jedis.close();
        }
        return result;
    }

    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedisResource();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            return result;
        }finally {
            jedis.close();
        }
        return result;
    }

    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedisResource();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error",key,e);
            return result;
        }finally {
            jedis.close();
        }
        return result;
    }

    public static Long setNx(String key,String value){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedisResource();
            result = jedis.setnx(key,value);
        } catch (Exception e) {
            log.error("setNx key:{} value:{} error",key,value,e);
            return result;
        }finally {
            jedis.close();
        }
        return result;
    }

    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedisResource();
        RedisShardedPoolUtil.set("keyTest","valueTest");
        String value = RedisShardedPoolUtil.get("keyTest");
        RedisShardedPoolUtil.setEx("keyex","valueex",60*10);

        RedisShardedPoolUtil.expire("keyTest",60*20);
        RedisShardedPoolUtil.del("keyTest");
        log.info("value"+value);
    }
}
