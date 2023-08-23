package com.scj.foolRpcClient;

import com.scj.foolRpcClient.testConsumer.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FRCApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(FRCApplication.class, args);
        Consumer consumer = run.getBean("consumer", Consumer.class);
        consumer.get();
    }

}
