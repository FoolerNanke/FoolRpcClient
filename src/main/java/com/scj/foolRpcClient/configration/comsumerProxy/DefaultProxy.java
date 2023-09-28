package com.scj.foolRpcClient.configration.comsumerProxy;

import com.scj.foolRpcBase.constant.Constant;
import com.scj.foolRpcBase.constant.RespCache;
import com.scj.foolRpcBase.entity.FoolProtocol;
import com.scj.foolRpcBase.entity.FoolRemoteReq;
import com.scj.foolRpcBase.exception.ExceptionEnum;
import com.scj.foolRpcBase.exception.FoolException;
import com.scj.foolRpcBase.handler.in.FoolProtocolDecode;
import com.scj.foolRpcBase.handler.out.FoolProtocolEncode;
import com.scj.foolRpcClient.annotation.FoolRpcConsumer;
import com.scj.foolRpcClient.constant.FRCConstant;
import com.scj.foolRpcClient.remote.FoolRegServer;
import com.scj.foolRpcClient.utils.CommonUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * @author suchangjie.NANKE
 * @Title: DefaultProxy
 * @date 2023/8/12 20:19
 * @description
 * 默认代理组件
 */

@Slf4j
@Component
public class DefaultProxy extends FoolProxy {

    @Autowired
    private FoolRegServer remoteServer;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws FoolException{
        /*
        通过全类名获取下游地址
         */
        String uniqueName = obj.getClass().getAnnotation(FoolRpcConsumer.class).uniqueName();
        String fullClassName = CommonUtil.buildName(obj.getClass().getName().split("\\$\\$")[0], uniqueName);
        // 获取版本
        String version = obj.getClass().getPackage().getImplementationVersion();
        // 从注册中心获取请求地址
        InetSocketAddress rpcAddress = remoteServer.getRpcAddress(fullClassName, version);
        /*
        获取连接通道
         */
        Channel clientChannel = getClientChannel(rpcAddress);
        /*
        构建请求
         */
        // 基础信息填充
        FoolProtocol<FoolRemoteReq> requestFoolProtocol = new FoolProtocol<>();
        // 设置请求类型
        requestFoolProtocol.setRemoteType(Constant.REMOTE_REQ);
        // 设置请求体
        FoolRemoteReq FoolRemoteReq = new FoolRemoteReq();
        // 填充请求参数
        FoolRemoteReq.setArgs(args);
        // 填充参数类型
        String[] argsType = new String[args.length];
        for (int i = 0; i < argsType.length; i++) {
            argsType[i] = method.getParameterTypes()[i].getName();
        }
        FoolRemoteReq.setArgsType(argsType);
        // 填充全类名
        FoolRemoteReq.setFullClassName(fullClassName);
        // 填充方法
        FoolRemoteReq.setMethodName(method.getName());
        // 将请求体填充进FoolProtocol中
        requestFoolProtocol.setData(FoolRemoteReq);
        // 根据该请求存储对应的Promise对象
        // 该Promise对象将用来存储响应返回值
        Promise<Object> foolResponsePromise = RespCache.handNewReq(requestFoolProtocol);
        /*
        发送请求
         */
        clientChannel.writeAndFlush(requestFoolProtocol);
        return foolResponsePromise;
    }

    /**
     * @param address 远程请求地址
     * @return 远程请求客户端
     */
    private Channel getClientChannel(InetSocketAddress address) {
        try {
            return new Bootstrap()
                    .group(FRCConstant.reqEventLoop)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new FoolProtocolEncode<>())
                                    .addLast(new FoolProtocolDecode())
                                    .addLast(FRCConstant.foolRespHandler);
                        }
                    }).connect(address).sync().channel();
        } catch (InterruptedException e) {
            log.error("远程链接发生中断异常 error:{} address:{}", e.getMessage(), address);
            throw new FoolException(ExceptionEnum.GENERATE_CLIENT_FAILED, e);
        }
    }
}
