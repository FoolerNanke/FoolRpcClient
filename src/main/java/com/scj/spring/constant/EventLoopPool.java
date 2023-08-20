package com.scj.spring.constant;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author suchangjie.NANKE
 * @Title: EventLoopPool
 * @date 2023/8/19 21:22
 * @description 线程池库对象
 */
public interface EventLoopPool {

    /**
     * netty 事件处理者
     * 继承自线程池
     */
    EventLoopGroup reqEventLoop = new NioEventLoopGroup(Constant.NUMBER_OF_REQ_WORKER);

    /**
     * 请求回执处理线程池
     */
    EventLoopGroup respPromiseEventLoop = new NioEventLoopGroup(Constant.NUMBER_OF_RESP_PROMISE_WORKER);
}
