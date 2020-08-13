package com.lowi.mqconsumer.mq;

import cn.hutool.json.JSONUtil;
import com.lowi.mqconsumer.pojo.dto.OrderDTO;
import com.lowi.mqconsumer.service.OrderService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * RocketMQServer.java
 * ==============================================
 * Copy right 2015-2018  by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @desc :  类
 * @author: gengyy
 * @version: v2.0.0
 * @since: 2018-5-7 11:50
 */
@Component
public class RocketMQServer {

    private static Logger logger = LoggerFactory.getLogger(RocketMQServer.class);

    private static Integer count = 0;
    /**
     * 消费者的组名
     */
    @Value("${apache.rocketmq.consumer.PushConsumer}")
    private String consumerGroup;
    @Autowired
    private OrderService orderService;
    /**
     * NameServer 地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${topic.list}")
    private String topicList; //从启动命令参数中获取主题列表参数值


    @PostConstruct
    public void defaultMQPushConsumer() {
        //消费者的组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);

        //指定NameServer地址，多个地址以 ; 隔开
        consumer.setNamesrvAddr(namesrvAddr);
        try {
            String[] topicArray = topicList.split(",");
            for (int i = 0; i < topicArray.length; i++) { //消费多个主题下的消息
                logger.info("topic = " + topicArray[i]);
                consumer.subscribe(topicArray[i], "*");
            }
            //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
            //如果非第一次启动，那么按照上次消费的位置继续消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
                try {
                    for (MessageExt messageExt : list) {
                        logger.info("messageExt: " + messageExt);//输出消息内容
                        String messageBody = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);
                        logger.info("消费响应：msgId : " + messageExt.getMsgId() + ",  msgBody : " + messageBody);//输出消息内容

                        String topic = messageExt.getTopic();
                        if ("order".equals(topic)) {
                            order(messageBody);
                            break;
                        }
                        if ("pay".equals(topic)) {
                            pay(messageBody);
                            break;
                        }
                        if ("delay".equals(topic)) {
                            delay(messageBody);
                            break;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
            });
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void order(String messageBody) {
        count = 0;
        logger.info("下订单");
        System.out.println("messageBody = " + messageBody);
    }

    public void delay(String messageBody) {
        OrderDTO orderDTO = JSONUtil.toBean(messageBody, OrderDTO.class);
        logger.info("延迟消费已启动:{}", messageBody);
        orderService.delayOrder(orderDTO);
    }

    public void pay(String messageBody) {
        count = 1;
        logger.info("支付订单");
    }

    public static void main(String[] args) {
        String data = null;
        OrderDTO orderDTO = JSONUtil.toBean(data, OrderDTO.class);
        boolean present = Optional.ofNullable(orderDTO).isPresent();
    }
}