package com.scj.foolRpcClient.remote;

import com.scj.foolRpcClient.configration.FoolRpcProperties;
import com.scj.foolRpcBase.constant.Constant;
import com.scj.foolRpcClient.constant.LocalCache;
import com.scj.foolRpcClient.constant.ObjectConstant;
import com.scj.foolRpcBase.entity.FoolProtocol;
import com.scj.foolRpcBase.entity.FoolRegisterReq;
import com.scj.foolRpcBase.entity.FoolRegisterResp;
import com.scj.foolRpcBase.exception.ExceptionEnum;
import com.scj.foolRpcBase.exception.FoolException;
import com.scj.foolRpcBase.handler.in.FoolProtocolDecode;
import com.scj.foolRpcClient.handler.FoolRegisterRespHandler;
import com.scj.foolRpcBase.handler.out.FoolProtocolEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
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
public class FoolRegServerImpl implements FoolRegServer, InitializingBean {

    /**
     * 注册中心链接通道
     */
    private Channel channel;

    @Autowired
    private FoolRpcProperties foolRpcProperties;

    @Override
    public InetSocketAddress getRpcAddress(String path, String version) {
        // return new InetSocketAddress("localhost", 5001);
        FoolProtocol<FoolRegisterReq> reqFoolProtocol = buildRegReq(path, version);
        // 填充请求类型
        reqFoolProtocol.setRemoteType(Constant.REGISTER_REQ_GET_IP);
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

    /**
     * 将Class信息注册到注册中心
     * @param fullClassName 全类名
     * @param version 版本
     */
    @Override
    public void registerClass(String fullClassName, String version) {
        FoolProtocol<FoolRegisterReq> reqFoolProtocol = buildRegReq(fullClassName, version);
        // 填充请求类型
        reqFoolProtocol.setRemoteType(Constant.REGISTER_REQ_REG_CLASS);
        // 写入
        channel.writeAndFlush(reqFoolProtocol);
    }

    /**
     * 构建请求体
     * @param path 全类名
     * @param version 版本
     */
    private FoolProtocol<FoolRegisterReq> buildRegReq(String path, String version){
        FoolProtocol<FoolRegisterReq> reqFoolProtocol = new FoolProtocol<>();
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
        return reqFoolProtocol;
    }

    @Override
    public void afterPropertiesSet() {
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
}
