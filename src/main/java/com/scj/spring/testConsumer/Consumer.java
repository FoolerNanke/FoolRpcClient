package com.scj.spring.testConsumer;

import com.scj.spring.annotation.FoolRpcConsumer;
import org.springframework.stereotype.Component;

/**
 * @author suchangjie.NANKE
 * @Title: Consumer
 * @date 2023/8/20 09:16
 * @description 消费者
 */

@Component
public class Consumer {

    @FoolRpcConsumer
    private Call call;

    public String get(){
        return call.testCall("hello ", "FoolRpc :", 0xF001);
    }
}
