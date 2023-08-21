package com.scj.spring.testConsumer;

import com.scj.spring.annotation.FoolRpcProvider;
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
    @Override
    public String testCall(String s1, String s2, int int1) {
        System.out.println(s1 + ";" + s2 + ";" +int1);
        return s1 + s2 + int1;
    }
}
