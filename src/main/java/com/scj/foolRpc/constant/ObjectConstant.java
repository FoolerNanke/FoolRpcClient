package com.scj.foolRpc.constant;

import com.scj.foolRpc.handler.in.FoolReqHandler;
import com.scj.foolRpc.handler.in.FoolRespHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author suchangjie.NANKE
 * @Title: StaticMap
 * @date 2023/8/19 21:00
 * @description 全局信息存储对象
 */
public class ObjectConstant {

    // ============ Netty Handler 相关常量实例 ============

    /**
     * 响应处理器
     */
    public static final FoolRespHandler foolRespHandler = new FoolRespHandler();

    public static final FoolReqHandler foolReqHandler = new FoolReqHandler();


    // ============ Netty EventLoop 相关常量实例 ============

    /**
     * netty 客户端发起的RPC请求事件处理线程池
     * 线程数量设置为 4 个
     */
    public static final EventLoopGroup reqEventLoop = new NioEventLoopGroup(Constant.NUMBER_OF_REQ_WORKER);

    /**
     * 请求回执信息处理线程池
     */
    public static final EventLoopGroup respPromiseEventLoop = new NioEventLoopGroup(Constant.NUMBER_OF_RESP_PROMISE_WORKER);
}
