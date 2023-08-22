package com.scj.foolRpc.configration.providerServer;

import com.scj.foolRpc.constant.Constant;
import com.scj.foolRpc.constant.ObjectConstant;
import com.scj.foolRpc.handler.in.FoolProtocolDecode;
import com.scj.foolRpc.handler.out.FoolProtocolEncode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Component;

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
                .group(ObjectConstant.reqEventLoop)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        channel.pipeline().addLast(new FoolProtocolDecode());
                        channel.pipeline().addLast(new FoolProtocolEncode<>());
                        channel.pipeline().addLast(ObjectConstant.foolReqHandler);
                    }
                }).bind(Constant.PORT);
    }

}
