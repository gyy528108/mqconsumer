package com.lowi.mqconsumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Lowi
 */
@SpringBootApplication
@MapperScan(basePackages = "com.lowi.mqconsumer.dao")
public class MqConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqConsumerApplication.class, args);
	}

}
