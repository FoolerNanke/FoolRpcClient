package com.scj.foolRpcClient.remote;

import com.scj.foolRpcClient.configration.FoolRpcProperties;
import com.scj.foolRpcClient.constant.Constant;
import com.scj.foolRpcClient.constant.ObjectConstant;
import com.scj.foolRpcClient.entity.FoolRegisterReq;
import com.scj.foolRpcClient.exception.ExceptionEnum;
import com.scj.foolRpcClient.exception.FoolException;
import com.scj.foolRpcClient.handler.in.FoolProtocolDecode;
import com.scj.foolRpcClient.handler.out.FoolProtocolEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author suchangjie.NANKE
 * @Title: RemoteServerImpl
 * @date 2023/8/19 22:19
 * @description 远程地址获得器
 */

@Slf4j
@Component
public class RemoteServerImpl implements RemoteServer{

    /**
     * 注册中心链接通道
     */
    private Channel channel;

    @Autowired
    private FoolRpcProperties foolRpcProperties;

    public RemoteServerImpl() {
        String registerIp = foolRpcProperties.getRegister_ip();
        if (registerIp == null || registerIp.equals("")){
            log.error("注册中心地址未填写");
            throw new FoolException(ExceptionEnum.REGISTER_ADDRESS_ERROR);
        }
        try {
            channel = new Bootstrap()
                    .group(ObjectConstant.RegisterEventLoop)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            // 编码器
                            channel.pipeline().addLast(new FoolProtocolEncode<>());
                            // 解码器
                            channel.pipeline().addLast(new FoolProtocolDecode());
                            // 响应处理器


                        }
                    }).connect(new InetSocketAddress(registerIp, Constant.PORT)).sync().channel();
        } catch (InterruptedException e) {
            log.error("无法链接到远程服务器");
            throw new FoolException(ExceptionEnum.GENERATE_CLIENT_FAILED, e);
        }

    }

    @Override
    public InetSocketAddress getRpcAddress(String path) {
        return new InetSocketAddress("localhost", 5001);
    }
}
