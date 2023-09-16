package com.scj.foolRpcClient.constant;

import com.scj.foolRpcBase.handler.resp.FoolRespHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author suchangjie.NANKE
 * @Title: StaticMap
 * @date 2023/8/19 21:00
 * @description 全局信息存储对象
 */
public class FRCConstant {

    // ============ 杂项参数 ==============
    /**
     * remote 请求处理数量
     */
    private static final int NUMBER_OF_REQ_WORKER = 8;

    /**
     * 注册中心 请求处理数量
     */
    private static final int NUMBER_OF_REGISTER_WORKER = 4;



    // ============ Netty Handler 相关常量实例 ============
    /**
     * 响应处理器
     */
    public static final FoolRespHandler foolRespHandler = new FoolRespHandler();

    // ============ Netty EventLoop 相关常量实例 ============
    /**
     * netty 客户端发起的RPC请求事件处理线程池
     * 线程数量设置为 4 个
     */
    public static final EventLoopGroup reqEventLoop
            = new NioEventLoopGroup(NUMBER_OF_REQ_WORKER);

    /**
     * 注册中心处理线程池
     */
    public static final EventLoopGroup RegisterEventLoop
            = new NioEventLoopGroup(NUMBER_OF_REGISTER_WORKER);
}
