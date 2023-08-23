package com.scj.foolRpcClient.serialize;

/**
 * @author suchangjie.NANKE
 * @Title: FoolSerialize
 * @date 2023/8/13 00:40
 * @description 序列化方法接口
 */
public interface FoolSerialize {

    /**
     * 序列化
     * @param obj 序列化对象
     * @return 序列化数据
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     * @param clazz 对象类型
     * @param data 序列化数据
     * @return 序列化后的对象
     */
    <T> T deSerialize(byte[] data, Class<T> clazz);
}
