package com.scj.foolRpcClient.handler;

import com.scj.foolRpcBase.constant.Constant;
import com.scj.foolRpcBase.runnable.PingPongHandler;
import com.scj.foolRpcClient.configration.SpringContextUtil;
import com.scj.foolRpcClient.remote.FoolRegServer;
import io.netty.channel.Channel;

import java.util.Random;

/**
 * @author: suchangjie.NANKE
 * @Title: ClientPingPongHandler
 * @date: 2023/9/16
 * @description: //TODO
 */
public class ClientPingPongHandler extends PingPongHandler {

    private static final Random rand = new Random();

    /**
     * 添加心跳请求托管
     * @param channel 通道
     * @param random 是否添加随机间隔时间
     */
    public static void addPingPong(Channel channel, boolean random){
        long gap = Constant.PING_PONG_TIME_GAP;
        if (random) {
            // 随机固定间隔
            gap += rand.nextInt((int) Constant.PING_PONG_TIME_GAP);
        }
        new ClientPingPongHandler(channel, gap);
    }

    private ClientPingPongHandler(Channel channel, long gap) {
        super(channel, gap);
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
