package com.scj.foolRpcClient.handler;

import com.scj.foolRpcClient.configration.SpringContextUtil;
import com.scj.foolRpcClient.remote.FoolRegServer;
import org.springframework.stereotype.Component;

/**
 * @author: suchangjie.NANKE
 * @Title: ErrorHandler
 * @date: 2023/9/28
 * @description:
 * 当出现无法连接到注册的情况时
 * 进行异常情况处理
 */
public class ErrorHandler {

    /**
     * 重新注册到注册中心
     */
    public static void reRegister(){
        FoolRegServer foolRegServer = (FoolRegServer) SpringContextUtil
                .getBeanByClazz(FoolRegServer.class);
        // 尝试重新连接
        foolRegServer.connect();
        // 重新注册
        foolRegServer.registerAgain();
    }
}
