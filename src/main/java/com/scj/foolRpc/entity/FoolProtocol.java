package com.scj.foolRpc.entity;

import com.scj.foolRpc.constant.Constant;
import lombok.Data;

import java.io.Serializable;
import java.util.Random;

/**
 * @author suchangjie.NANKE
 * @Title: FoolProtocol
 * @date 2023/8/12 23:41
 * @description 协议类
 */
@Data
public class FoolProtocol<T> implements Serializable {

    // 魔数
    private short magic = Constant.MAGIC;

    // 版本号
    private byte version = Constant.VERSION;

    // 序列化方法
    private byte SerializableType = Constant.HESSIAN;

    // 请求类型
    private byte remoteType;

    // 请求 ID
    private Long reqId = new Random().nextLong();

    // 请求数据
    private T data;
}
