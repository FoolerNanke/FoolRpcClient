package com.scj.foolRpcClient.constant;

/**
 * @author suchangjie.NANKE
 * @Title: Constant
 * @date 2023/8/12 20:46
 * @description 常量定义类
 */
public interface Constant {

    // ============= 杂项参数 ================

    /**
     * 消费者进行远程链接时的线程处理数量
     */
    int NUMBER_OF_REQ_WORKER = 4;

    /**
     * 消费者进行远程链接时的线程处理数量
     */
    int NUMBER_OF_RESP_PROMISE_WORKER = 4;

    /**
     * 注册中心链接处理的线程处理数量
     */
    int NUMBER_OF_REGISTER_WORKER = 4;

    /**
     * 字符串间隔
     */
    String GAP_POINT = ".";

    // ============= 请求类型 ================

    /**
     * 发送给注册中心的请求
     */
    byte REGISTER_REQ = 1;

    /**
     * 来自注册中心的响应
     */
    byte REGISTER_RESP = 2;

    /**
     * 发送给远程下游的请求
     */
    byte REMOTE_REQ = 11;

    /**
     * 来自远程下游的响应
     */
    byte REMOTE_RESP = 12;

    // ============= 序列化方法 ================

    /**
     * hessian 序列化方法
     */
    byte HESSIAN = 0;

    /**
     * jdk 序列化方法
     */
    byte JDK = 2;

    // ============= 请求基本参数 ================

    /**
     * 魔数
     */
    short MAGIC = (short) 0xF001;

    /**
     * 版本号
     */
    byte VERSION = 0x00;

    /**
     * 消息头长度
     * magic(short) + version(byte) + remoteType(byte)
     * + SerializableType(byte) + dataLen(int) + reqId(long)
     */
    int HEADER_LENGTH = 17;

    /**
     * 服务提供端口
     */
    int PORT = 5001;
}
