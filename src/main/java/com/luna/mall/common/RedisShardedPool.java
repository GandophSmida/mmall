package com.luna.mall.common;

import com.luna.mall.util.PropertiesUtil;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool jedisPool; //sharded jedis连接池
    private static Integer maxTotal=Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20")); //最大连接数
    private static Integer maxIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10")); //最大空闲连接数
    private static Integer minIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2")); //最小空闲连接数
    private static Boolean testOnBorrow=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.max.total","true")); //取用校验
    private static Boolean testOnReturn=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.max.total","true")); //归还校验
    private static String redis1Ip=PropertiesUtil.getProperty("redis1.ip"); //redis1 IP
    private static Integer redis1Port=Integer.parseInt(PropertiesUtil.getProperty("redis1.port")); //reids1 Port
    private static String redis2Ip=PropertiesUtil.getProperty("redis2.ip"); //redis2 IP
    private static Integer redis2Port=Integer.parseInt(PropertiesUtil.getProperty("redis2.port")); //reids2 Port
    private static void initPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        jedisPoolConfig.setBlockWhenExhausted(true); //无可用redis连接时是否阻塞

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip,redis1Port,1000*2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,1000*2);
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);
        jedisPool = new ShardedJedisPool(jedisPoolConfig,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initPool();
    }

    public static ShardedJedis getJedisResource(){
        return jedisPool.getResource();
    }

    @Deprecated
    public static void returnJedisBrokenResource(ShardedJedis jedis){
        jedisPool.returnBrokenResource(jedis);
    }

    @Deprecated
    public static void returnJedisResource(ShardedJedis jedis){
        jedisPool.returnResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = getJedisResource();
        for(int i=0;i<10;i++){
            jedis.set("key"+i,"value"+i);
        }
        jedis.close(); //替代了returnJedisBrokenResource和returnJedisResource
        System.out.println("Program is end!");
    }
}
