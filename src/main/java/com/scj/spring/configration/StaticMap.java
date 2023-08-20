package com.scj.spring.configration;

import com.scj.spring.constant.Constant;
import com.scj.spring.constant.EventLoopPool;
import com.scj.spring.entity.FoolProtocol;
import com.scj.spring.entity.FoolRequest;
import com.scj.spring.entity.FoolResponse;
import com.scj.spring.handler.in.FoolProtocolDecode;
import com.scj.spring.handler.in.FoolRespHandler;
import com.scj.spring.handler.out.FoolProtocolEncode;
import com.scj.spring.serialize.FoolSerialize;
import com.scj.spring.serialize.HessianSerialize;
import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.DefaultPromise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author suchangjie.NANKE
 * @Title: StaticMap
 * @date 2023/8/19 21:00
 * @description 全局信息存储对象
 */
public class StaticMap {

    public static ChannelHandler[] handlers = new ChannelHandler[]{
            new FoolProtocolEncode<>(), new FoolProtocolDecode(),
            new FoolRespHandler()};

    /**
     * 序列化存储对象
     */
    private static Map<Byte, FoolSerialize> SerializeMap = new ConcurrentHashMap<>();

    static {
        SerializeMap.put(Constant.HESSIAN, new HessianSerialize());
    }
    /**
     * 请求回复 promise 存储对象
     */
    private static ConcurrentMap<Long, Promise<FoolResponse>> PromiseMap = new ConcurrentHashMap<>();

    /**
     * 获取序列化实例
     * @param type 序列化类型
     * @return 序列化实例
     */
    public static FoolSerialize getFoolSerialize(byte type){
        FoolSerialize serialize = SerializeMap.getOrDefault(type, null);
        if (serialize == null) return getFoolSerialize((byte) 0);
        return serialize;
    }

    /**
     * 存储请求:Promise对象
     * @param foolProtocol
     */
    public static Promise<FoolResponse> delNewReq(FoolProtocol<FoolRequest> foolProtocol){
        Promise<FoolResponse> promise = new DefaultPromise<>(EventLoopPool.respPromiseEventLoop.next());
        PromiseMap.put(foolProtocol.getReqId(), promise);
        return promise;
    }

    public static Promise<FoolResponse> getPromise(FoolProtocol<FoolResponse> foolProtocol){
        return PromiseMap.remove(foolProtocol.getReqId());
    }
}
