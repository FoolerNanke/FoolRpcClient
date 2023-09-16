package com.scj.foolRpcClient.handler;

import com.scj.foolRpcBase.runnable.PingPongHandler;
import com.scj.foolRpcClient.configration.SpringContextUtil;
import com.scj.foolRpcClient.remote.FoolRegServer;
import io.netty.channel.Channel;

/**
 * @author: suchangjie.NANKE
 * @Title: ClientPingPongRunnable
 * @date: 2023/9/16
 * @description: //TODO
 */
public class ClientPingPongHandler extends PingPongHandler {

    /**
     * 添加心跳请求托管
     * @param channel 通道
     */
    public static void addPingPong(Channel channel){
        new ClientPingPongHandler(channel);
    }

    private ClientPingPongHandler(Channel channel) {
        super(channel);
    }

    @Override
    public void handError(Throwable t) {
        super.handError(t);
        // 心跳失败 说明注册中心挂掉了
        // 则需要重新连接注册中心
        FoolRegServer foolRegServer = (FoolRegServer)SpringContextUtil
                .getBeanByClazz(FoolRegServer.class);
        // 尝试重新连接
        foolRegServer.connect();
        // 重新注册
        foolRegServer.registerAgain();
    }
}
