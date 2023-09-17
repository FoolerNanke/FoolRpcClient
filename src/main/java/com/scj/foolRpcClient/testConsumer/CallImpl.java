package com.scj.foolRpcClient.testConsumer;

import com.scj.foolRpcClient.annotation.FoolRpcProvider;
import com.scj.foolRpcClient.configration.FoolRpcProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author suchangjie.NANKE
 * @Title: CallImpl
 * @date 2023/8/20 18:39
 * @description call ipl
 */

@Component
@FoolRpcProvider
public class CallImpl implements Call{

//    @Value("${app.version}")

    @Autowired
    private FoolRpcProperties foolRpcProperties;
    @Override
    public void testCall(String s1, String s2, int int1) {
        System.out.println(s1 + ";" + s2 + ";" +int1);
    }

    public FoolRpcProperties getFoolRpcProperties() {
        return foolRpcProperties;
    }

    public void setFoolRpcProperties(FoolRpcProperties foolRpcProperties) {
        this.foolRpcProperties = foolRpcProperties;
    }
}
