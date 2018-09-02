package com.luna.mall.task;

import com.luna.mall.serivce.OrderService;
import com.luna.mall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CloseOrderTask {
    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 */1 * * * ?") //每个1分钟的整数倍
    public void closeOrderTaskV1(){
        log.info("关闭订单定时任务开启");
        int hour = PropertiesUtil.getIntProperty("close.order.task.time.hour","2");
        orderService.closeOrder(hour);
        log.info("关闭订单定时任务完成");
    }
}
