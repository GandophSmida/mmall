package com.luna.mall.common;

import com.luna.mall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class RedissonManager {
    private Config config = new Config();
    private Redisson redisson = null;
    private static String redis1Ip=PropertiesUtil.getProperty("redis1.ip"); //redis1 IP
    private static Integer redis1Port=Integer.parseInt(PropertiesUtil.getProperty("redis1.port")); //reids1 Port
    private static String redis2Ip=PropertiesUtil.getProperty("redis2.ip"); //redis2 IP
    private static Integer redis2Port=Integer.parseInt(PropertiesUtil.getProperty("redis2.port")); //reids2 Port

    public Redisson getRedisson() {
        return redisson;
    }

    @PostConstruct
    private void init(){
        try {
            log.info("初始化redis开始");
            config.useSingleServer().setAddress(new StringBuilder().append(redis1Ip).append(":").append(redis1Port).toString());
            redisson = (Redisson)Redisson.create(config);
            log.info("初始化redis结束");
        } catch (Exception e) {
            log.error("redis init error",e);
        }
    }
}
