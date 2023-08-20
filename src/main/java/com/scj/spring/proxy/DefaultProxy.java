package com.scj.spring.proxy;

import com.scj.spring.configration.StaticMap;
import com.scj.spring.constant.Constant;
import com.scj.spring.constant.EventLoopPool;
import com.scj.spring.entity.FoolProtocol;
import com.scj.spring.entity.FoolRequest;
import com.scj.spring.entity.FoolResponse;
import com.scj.spring.handler.in.FoolProtocolDecode;
import com.scj.spring.handler.out.FoolProtocolEncode;
import com.scj.spring.remote.RemoteServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author suchangjie.NANKE
 * @Title: DefaultProxy
 * @date 2023/8/12 20:19
 * @description
 * 默认代理组件
 */
@Component
public class DefaultProxy extends AbstractFoolProxy {

//    private final Logger logger = LoggerFactory.getLogger(DefaultProxy.class);

    @Autowired
    private RemoteServer remoteServer;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        /*
        通过全类名获取下游地址
         */
        String fullClassName = String.join(Constant.GAP_POINT,
                obj.getClass().getPackageName(), obj.getClass().getName());
        InetSocketAddress rpcAddress = remoteServer.getRpcAddress(fullClassName);
        /*
        获取连接通道
         */
        Channel clientChannel = getClientChannel(rpcAddress, StaticMap.handlers);
        /*
        构建请求
         */
        // 基础信息填充
        FoolProtocol<FoolRequest> requestFoolProtocol = new FoolProtocol<>();
        requestFoolProtocol.setRemoteType(Constant.REMOTE_REQ);

        FoolRequest foolRequest = new FoolRequest();
        foolRequest.setArgs(args);
        foolRequest.setFullClassName(fullClassName);
        foolRequest.setMethodName(method.getName());
        requestFoolProtocol.setData(foolRequest);
        /*
        发送请求
         */
        Promise<FoolResponse> foolResponsePromise = StaticMap.delNewReq(requestFoolProtocol);
        clientChannel.writeAndFlush(requestFoolProtocol);
        return foolResponsePromise;
    }

    /**
     * @param address 远程请求地址
     * @param handlers 处理流程
     * @return 远程请求客户端
     */
    public Channel getClientChannel(InetSocketAddress address, ChannelHandler...handlers) {
        try {
            return new Bootstrap()
                    .group(EventLoopPool.reqEventLoop)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) {
                        Iterator<ChannelHandler> iterator = Arrays.stream(handlers).iterator();
                        while (iterator.hasNext()){
                            ChannelHandler next = iterator.next();
                            channel.pipeline().addLast(next);
                        }
                        }
                    }).connect(address).sync().channel();
        } catch (Exception e) {
            //logger.info("getClientChannel error {}", e.getMessage());
            System.out.println(e);
        }
        return null;
    }
}
