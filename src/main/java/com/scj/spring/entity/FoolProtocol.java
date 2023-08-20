package com.scj.spring.entity;

import com.scj.spring.constant.Constant;
import io.netty.util.concurrent.Promise;
import lombok.Data;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

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

    // 请求 ID
    private Long reqId = new Random().nextLong();

    // 请求类型
    private byte remoteType;

    // 请求数据
    private T data;
}
