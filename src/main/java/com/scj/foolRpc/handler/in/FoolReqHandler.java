package com.scj.foolRpc.handler.in;

import com.scj.foolRpc.constant.Constant;
import com.scj.foolRpc.constant.LocalCache;
import com.scj.foolRpc.entity.FoolProtocol;
import com.scj.foolRpc.entity.FoolRequest;
import com.scj.foolRpc.entity.FoolResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

/**
 * @author suchangjie.NANKE
 * @Title: FoolReqHandler
 * @date 2023/8/21 21:44
 * @description 请求处理器
 */

@ChannelHandler.Sharable
public class FoolReqHandler  extends SimpleChannelInboundHandler<FoolProtocol<FoolRequest>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FoolProtocol<FoolRequest> foolRequestFoolProtocol) throws Exception {
        FoolRequest request = foolRequestFoolProtocol.getData();
        // 根据请求携带全类名数据
        // 获取处理该请求的实例
        Object bean = LocalCache.get(request.getFullClassName());
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
            System.out.println("error");
            return;
        }
        // 强制访问方法
        method.setAccessible(true);
        // 调用方法
        Object invoke = method.invoke(bean, request.getArgs());
        // 填充响应基本信息
        FoolProtocol<FoolResponse> responseFoolProtocol = new FoolProtocol<>();
        responseFoolProtocol.setReqId(foolRequestFoolProtocol.getReqId());
        responseFoolProtocol.setRemoteType(Constant.REMOTE_RESP);
        FoolResponse response = new FoolResponse();
        responseFoolProtocol.setData(response);
        // 填充响应数据信息
        response.setFullClassName(method.getReturnType().getName());
        response.setData(invoke);
        ctx.writeAndFlush(responseFoolProtocol);
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
