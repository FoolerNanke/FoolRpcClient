package com.scj.foolRpcClient.remote;

import com.scj.foolRpcClient.configration.FoolRpcProperties;
import com.scj.foolRpcClient.constant.Constant;
import com.scj.foolRpcClient.constant.LocalCache;
import com.scj.foolRpcClient.constant.ObjectConstant;
import com.scj.foolRpcClient.entity.FoolProtocol;
import com.scj.foolRpcClient.entity.FoolRegisterReq;
import com.scj.foolRpcClient.entity.FoolRegisterResp;
import com.scj.foolRpcClient.exception.ExceptionEnum;
import com.scj.foolRpcClient.exception.FoolException;
import com.scj.foolRpcClient.handler.in.FoolProtocolDecode;
import com.scj.foolRpcClient.handler.in.FoolRegisterRespHandler;
import com.scj.foolRpcClient.handler.out.FoolProtocolEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private final Channel channel;

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
                        protected void initChannel(NioSocketChannel channel) {
                            channel.pipeline()
                                // 编码器
                                .addLast(new FoolProtocolEncode<>())
                                // 解码器
                                .addLast(new FoolProtocolDecode())
                                // 响应处理器
                                .addLast(new FoolRegisterRespHandler());
                        }
                    }).connect(new InetSocketAddress(registerIp, Constant.PORT)).sync().channel();
        } catch (InterruptedException e) {
            log.error("无法链接到远程服务器");
            throw new FoolException(ExceptionEnum.GENERATE_CLIENT_FAILED, e);
        }

    }

    @Override
    public InetSocketAddress getRpcAddress(String path, String version) {
        FoolProtocol<FoolRegisterReq> reqFoolProtocol = new FoolProtocol<>();
        reqFoolProtocol.setRemoteType(Constant.REGISTER_REQ);
        // 请求体
        FoolRegisterReq foolRegisterReq = new FoolRegisterReq();
        // 填充请求体
        reqFoolProtocol.setData(foolRegisterReq);
        // 填充全类名
        foolRegisterReq.setFullClassName(path);
        // 填充版本号
        foolRegisterReq.setVersion(version);
        // 填充时间戳
        foolRegisterReq.setTimeStamp(System.currentTimeMillis());
        // 填充应用名
        foolRegisterReq.setAppName(foolRegisterReq.getAppName());
        // 根据该请求存储对应的Promise对象
        // 该Promise对象将用来存储响应返回值
        Promise<Object> foolResponsePromise = LocalCache.handNewReq(reqFoolProtocol);
        // 写入
        channel.writeAndFlush(reqFoolProtocol);
        try {
            FoolRegisterResp resp = (FoolRegisterResp)foolResponsePromise.get(Constant.TIME_OUT, TimeUnit.MICROSECONDS);
            if (resp.getIP() == null || resp.getIP().equals("")){
                log.error("无法获取服务提供地址");
                throw new FoolException(resp.getCode(), resp.getMessage());
            }
            // 返回下游地址
            return new InetSocketAddress(resp.getIP(), Constant.PORT);
        } catch (InterruptedException
                 | ExecutionException
                 | TimeoutException e) {
            log.error("无法获取服务提供地址");
            throw new FoolException(ExceptionEnum.GET_REMOTE_SERVER_ERROR, e);
        }
    }
}
