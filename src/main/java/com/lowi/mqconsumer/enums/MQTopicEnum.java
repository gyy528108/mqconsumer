package com.lowi.mqconsumer.enums;

import lombok.Getter;

/**
 * ==============================================
 * Copy right 2015-2018  by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @desc :  mq枚举类
 * @author: gengyy
 * @version: v2.0.0
 * @since: 2019-10-10 15:42
 */
@Getter
public enum MQTopicEnum {
    //订单
    ORDER("order"),
    //支付
    PAY("pay"),
    //取消
    DALEY("daley"),
    ;

    MQTopicEnum(String topic) {
        this.topic = topic;
    }

    private String topic;
}
