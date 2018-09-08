package com.luna.mall.common;

import com.luna.mall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    private static JedisPool jedisPool; //jedis连接池
    private static Integer maxTotal=Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20")); //最大连接数
    private static Integer maxIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10")); //最大空闲连接数
    private static Integer minIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2")); //最小空闲连接数
    private static Boolean testOnBorrow=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.max.total","true")); //取用校验
    private static Boolean testOnReturn=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.max.total","true")); //归还校验
    private static String redisIp=PropertiesUtil.getProperty("redis1.ip"); //redis1 IP
    private static Integer redisPort=Integer.parseInt(PropertiesUtil.getProperty("redis1.port")); //redis1 Port
    private static void initPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        jedisPoolConfig.setBlockWhenExhausted(true); //无可用redis连接时是否阻塞
        jedisPool = new JedisPool(jedisPoolConfig,redisIp,redisPort,1000*2);
    }

    static {
        initPool();
    }

    public static Jedis getJedisResource(){
        return jedisPool.getResource();
    }

    @Deprecated
    public static void returnJedisBrokenResource(Jedis jedis){
        jedisPool.returnBrokenResource(jedis);
    }

    @Deprecated
    public static void returnJedisResource(Jedis jedis){
        jedisPool.returnResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = jedisPool.getResource();
        System.out.println(jedis.ping());
        jedis.set("name","luna");
        jedis.close(); //替代了returnJedisBrokenResource和returnJedisResource
        jedisPool.destroy(); //临时调用，销毁连接池中的所有连接
        System.out.println("Program is end!");
    }
}
