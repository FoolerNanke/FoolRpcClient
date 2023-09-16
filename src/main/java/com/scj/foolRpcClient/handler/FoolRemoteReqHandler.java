package com.scj.foolRpcClient.handler;

import com.scj.foolRpcBase.constant.Constant;
import com.scj.foolRpcBase.entity.FoolRemoteReq;
import com.scj.foolRpcBase.entity.FoolRemoteResp;
import com.scj.foolRpcClient.configration.providerServer.ProviderService;
import com.scj.foolRpcBase.entity.FoolProtocol;
import com.scj.foolRpcBase.exception.ExceptionEnum;
import com.scj.foolRpcBase.exception.FoolException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author suchangjie.NANKE
 * @Title: FoolRemoteReqHandler
 * @date 2023/8/21 21:44
 * @description 请求处理器
 */

@Slf4j
@ChannelHandler.Sharable
@Component
public class FoolRemoteReqHandler extends SimpleChannelInboundHandler<FoolProtocol<FoolRemoteReq>> {

    @Autowired
    private ProviderService providerService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FoolProtocol<FoolRemoteReq> protocol) throws Exception {
        FoolRemoteReq request = protocol.getData();
        // 根据请求携带全类名数据
        // 获取处理该请求的实例
        Object bean = providerService.get(request.getFullClassName());
        // 获取请求携带的入参
        String[] argsType = request.getArgsType();
        Method method = null;
        for (Method m : bean.getClass().getMethods()) {
            if (m.getName().equals(request.getMethodName()) &&
                argsType.length == m.getParameterTypes().length &&
                isSameMethod(m.getParameterTypes(), argsType)){
                method = m;
                break;
            }
        }
        if (method == null){
            log.error("远程请求类名为{}, 请求方法名为{}, 请求参数类型为{}, 无法在对应类下找到该方法",
                    request.getFullClassName(), request.getMethodName(), Arrays.toString(request.getArgsType()));
            throw new FoolException(ExceptionEnum.METHOD_NOT_EXIST);
        }
        // 强制访问方法
        method.setAccessible(true);
        // 调用方法
        Object invoke = method.invoke(bean, request.getArgs());
        // 填充响应基本信息
        FoolProtocol<FoolRemoteResp> responseFoolProtocol = new FoolProtocol<>();
        responseFoolProtocol.setReqId(protocol.getReqId());
        responseFoolProtocol.setRemoteType(Constant.REMOTE_RESP);
        FoolRemoteResp response = new FoolRemoteResp();
        responseFoolProtocol.setData(response);
        // 填充响应数据信息
        response.setFullClassName(method.getReturnType().getName());
        response.setData(invoke);
        ctx.writeAndFlush(responseFoolProtocol);
        ctx.fireChannelRead(protocol);
    }

    /**
     * 判断是否式同一个方法
     * @param parameterTypes 本地参数类型数组
     * @param argsType 上游传递的参数类型数组
     * @return Boolean
     */
    public boolean isSameMethod(Class<?>[] parameterTypes,String[] argsType){
        for (int i = 0; i < argsType.length; i++) {
            if (!parameterTypes[i].getName().equals(argsType[i])){
                return false;
            }
        }
        return true;
    }
}
