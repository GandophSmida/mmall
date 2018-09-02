package com.luna.mall.task;

import com.luna.mall.common.Constants;
import com.luna.mall.serivce.OrderService;
import com.luna.mall.util.PropertiesUtil;
import com.luna.mall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Slf4j
public class CloseOrderTask {
    @Autowired
    private OrderService orderService;

    /**
     * Tomcat温柔的关闭，即执行shutdown命令时会调用注解@PreDestroy的方法，如果直接kill进程则不会执行该方法，即@PreDestroy会失效
     */
    @PreDestroy
    public void delLock(){
        RedisShardedPoolUtil.del(Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

//    @Scheduled(cron = "0 */1 * * * ?") //每个1分钟的整数倍
    public void closeOrderTaskV1(){
        log.info("关闭订单定时任务开启");
        int hour = PropertiesUtil.getIntProperty("close.order.task.time.hour","2");
        orderService.closeOrder(hour);
        log.info("关闭订单定时任务完成");
    }

//    @Scheduled(cron = "0 */1 * * * ?") //每个1分钟的整数倍
    public void closeOrderTaskV2(){
        log.info("关闭订单定时任务开启");
        long lockTimeOut = Long.parseLong(PropertiesUtil.getProperty("lock.time.out","5000"));
        Long setNxResult = RedisShardedPoolUtil.setNx(Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeOut));
        if(setNxResult != null && setNxResult.intValue() == 1){
            //返回值为1代表设置成功，获取锁
            closeOrder(Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else{
            log.info("没有获得分布式锁：{}"+Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("关闭订单定时任务完成");
    }

    @Scheduled(cron = "0 */1 * * * ?") //每个1分钟的整数倍
    public void closeOrderTaskV3(){
        log.info("关闭订单定时任务开启");
        long lockTimeOut = Long.parseLong(PropertiesUtil.getProperty("lock.time.out","5000"));
        Long setNxResult = RedisShardedPoolUtil.setNx(Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeOut));
        if(setNxResult != null && setNxResult.intValue() == 1){
            closeOrder(Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else{
            //未获取到锁继续判断，判断时间戳看是否可以重置并获取到锁
            String lockValueStr = RedisShardedPoolUtil.get(Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if(lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){
                String getSetResult = RedisShardedPoolUtil.getSet(Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeOut));
                /**
                 * 再次用当前时间戳getSet，返回给定key的旧值，根据返回的值进一步判断是否可以获取锁：
                 * 当key没有旧值，即key不存在时，返回nil(jedis中的nil即相当于Java中的null) ->
                 * 获取分布式锁.
                 */
                if(getSetResult == null || (getSetResult != null && StringUtils.equals(getSetResult,lockValueStr))){
                    //真正获取到锁
                    closeOrder(Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }else{
                    log.info("没有获得分布式锁：{}"+Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }else{
                log.info("没有获得分布式锁：{}"+Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }
        log.info("关闭订单定时任务完成");
    }

    private void closeOrder(String lockName){
        RedisShardedPoolUtil.expire(lockName,50); //有效期50秒防止死锁
        log.info("获取{},ThreadName:{}",Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        int hour = PropertiesUtil.getIntProperty("close.order.task.time.hour","2");
//        orderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{},ThreadName:{}",Constants.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        log.info("======================================");
    }
}
