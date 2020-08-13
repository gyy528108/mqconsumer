package com.lowi.mqconsumer.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lowi.mqconsumer.dao.OrderDao;
import com.lowi.mqconsumer.entity.Order;
import com.lowi.mqconsumer.enums.OrderStatusEnum;
import com.lowi.mqconsumer.pojo.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OrderService.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/12 18:03
 */
@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void delayOrder(OrderDTO orderDTO) {
        if (orderDTO.getOrderId() == null) {
            logger.info("消费失败：{}", orderDTO);
            return;
        }
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderDTO.getOrderId());
        Order order = orderDao.selectOne(queryWrapper);
        if (order == null) {
            logger.info("消费失败：{}", orderDTO);
            return;
        }
        boolean statusNormal = (OrderStatusEnum.DELAY.getStatus().equals(order.getStatus()) || OrderStatusEnum.PAY.getStatus().equals(order.getStatus()));
        if (statusNormal) {
            logger.info("消费失败：{}", order);
            return;
        }
        orderDao.delayOrder(order.getId(), order.getVersion());
    }
}
