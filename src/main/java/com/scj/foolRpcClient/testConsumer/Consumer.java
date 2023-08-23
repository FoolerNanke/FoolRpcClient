package com.scj.foolRpcClient.testConsumer;

import com.scj.foolRpcClient.annotation.ConsumeType;
import com.scj.foolRpcClient.annotation.FoolRpcConsumer;
import org.springframework.stereotype.Component;

/**
 * @author suchangjie.NANKE
 * @Title: Consumer
 * @date 2023/8/20 09:16
 * @description 消费者
 */

@Component
public class Consumer {

    @FoolRpcConsumer(consumeType = ConsumeType.CALLBACK, CallBackMethod = "callBack.callBack1")
    private Call call;

    public String get(){
        call.testCall("hello ", "FoolRpc :", 0xF001);
        return "1";
    }
}
