package com.scj.spring.provider;

import com.scj.spring.constant.Constant;
import com.scj.spring.constant.EventLoopPool;
import com.scj.spring.entity.FoolProtocol;
import com.scj.spring.entity.FoolRequest;
import com.scj.spring.entity.FoolResponse;
import com.scj.spring.handler.in.FoolProtocolDecode;
import com.scj.spring.handler.out.FoolProtocolEncode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author suchangjie.NANKE
 * @Title: Provider
 * @date 2023/8/20 13:40
 * @description 服务提供方
 */

@Component
public class Provider {

    public Provider(){
        new ServerBootstrap()
                .group(EventLoopPool.reqEventLoop)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new FoolProtocolDecode());
                        channel.pipeline().addLast(new FoolProtocolEncode<>());
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                                FoolProtocol<FoolRequest> foolProtocol =(FoolProtocol<FoolRequest>)msg;
                                Object bean = ProviderMap.get(foolProtocol.getData().getFullClassName());
                                Object[] args = foolProtocol.getData().getArgs();
                                Class<?>[] clazzs = new Class[args.length];
                                for (int i = 0; i < clazzs.length; i++) {
                                    clazzs[i] = args[i].getClass();
                                }
                                Method method = null;
                                try {
                                    method = bean.getClass().getMethod(foolProtocol.getData().getMethodName(), clazzs);
                                }catch (Exception e){
                                    for (Method m : bean.getClass().getMethods()) {
                                        if (m.getName().equals(foolProtocol.getData().getMethodName())){
                                            method = m;
                                            break;
                                        }
                                    }
                                }
                                if (method == null) return;
                                method.setAccessible(true);
                                Object invoke = method.invoke(bean, foolProtocol.getData().getArgs());
//                                System.out.println(invoke);

                                FoolProtocol<FoolResponse> responseFoolProtocol = new FoolProtocol<>();
                                responseFoolProtocol.setReqId(foolProtocol.getReqId());
                                responseFoolProtocol.setRemoteType(Constant.REMOTE_RESP);
                                FoolResponse response = new FoolResponse();
                                responseFoolProtocol.setData(response);
                                response.setFullClassName(invoke.getClass().getName());
                                response.setData(invoke);

                                ctx.writeAndFlush(responseFoolProtocol);
                            }
                        });
                    }
                }).bind(Constant.PORT);
    }

}
