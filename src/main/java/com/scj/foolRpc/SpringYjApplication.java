package com.scj.foolRpc;

import com.scj.foolRpc.testConsumer.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringYjApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SpringYjApplication.class, args);
        Consumer consumer = run.getBean("consumer", Consumer.class);
        consumer.get();
    }

}
