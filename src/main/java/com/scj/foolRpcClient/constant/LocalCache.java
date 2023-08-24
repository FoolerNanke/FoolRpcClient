package com.scj.foolRpcClient.constant;

import com.scj.foolRpcClient.entity.FoolProtocol;
import com.scj.foolRpcClient.entity.FoolRequest;
import com.scj.foolRpcClient.entity.FoolResponse;
import com.scj.foolRpcClient.serialize.FoolSerialize;
import com.scj.foolRpcClient.serialize.HessianSerialize;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author suchangjie.NANKE
 * @Title: LocalCache
 * @date 2023/8/21 21:04
 * @description 本地缓存
 */
public class LocalCache {

    /**
     * 序列化存储对象
     * key:SerializableType
     * value:FoolSerialize->Bean
     */
    private static final Map<Byte, FoolSerialize> SerializeMap = new ConcurrentHashMap<>();

    /**
     * 请求回复 promise 存储对象
     * key:reqId
     * value:Promise<FoolResponse>
     */
    private static final Map<Long, Promise<Object>> PromiseMap = new ConcurrentHashMap<>();

    /**
     * 服务提供方存储对象
     * key:fullClassName
     * value:Bean
     */
    private static final Map<String, Object> ProviderMap = new ConcurrentHashMap<>();

    static {
        /*
         初始化 SerializeMap
         */
        SerializeMap.put(Constant.HESSIAN, new HessianSerialize());
    }

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
     * @param foolProtocol 存储协议
     */
    public static Promise<Object> handNewReq(FoolProtocol<?> foolProtocol){
        Promise<Object> promise = new DefaultPromise<>(ObjectConstant.respPromiseEventLoop.next());
        PromiseMap.put(foolProtocol.getReqId(), promise);
        return promise;
    }

    /**
     * 根据响应获取对应的PromiseMap对象
     * @param foolProtocol 响应
     * @return Promise<FoolResponse>
     */
    public static Promise<Object> getPromise(FoolProtocol<?> foolProtocol){
        return PromiseMap.remove(foolProtocol.getReqId());
    }

    /**
     * 新增响应实例
     * @param name 全类名
     * @param obj 对象
     */
    public static void put(String name, Object obj){
        ProviderMap.put(name, obj);
    }

    /**
     * 获取响应实例
     * @param name 全类名
     * @return Object->Bean
     */
    public static Object get(String name){
        return ProviderMap.get(name);
    }
}
