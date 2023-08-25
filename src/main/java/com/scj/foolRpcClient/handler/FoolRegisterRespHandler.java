package com.scj.foolRpcClient.handler;

import com.scj.foolRpcBase.constant.Constant;
import com.scj.foolRpcClient.constant.LocalCache;
import com.scj.foolRpcBase.entity.FoolProtocol;
import com.scj.foolRpcBase.entity.FoolRegisterResp;
import com.scj.foolRpcBase.exception.ExceptionEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suchangjie.NANKE
 * @Title: FoolRegisterRespHandler
 * @date 2023/8/24 23:01
 * @description 注册中心响应处理器
 */

@Slf4j
public class FoolRegisterRespHandler extends SimpleChannelInboundHandler<FoolProtocol<Object>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext
            , FoolProtocol<Object> foolProtocol) {
        byte remoteType = foolProtocol.getRemoteType();
        switch (remoteType){
            // 获取IP地址的请求
            case Constant.REGISTER_RESP_IP:
                Promise<Object> promise = LocalCache.getPromise(foolProtocol);
                promise.setSuccess(foolProtocol.getData());
                return;
            // 注册bean
            case Constant.REGISTER_REQ_REG_CLASS:
                FoolRegisterResp data = (FoolRegisterResp)foolProtocol.getData();
                if (!data.getCode().equals(ExceptionEnum.SUCCESS.getErrorCode())){
                    log.error("注册异常, code:{} message:{}", data.getCode(), data.getMessage());
                }
                break;
        }
    }
}
