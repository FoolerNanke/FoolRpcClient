package com.scj.spring.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author suchangjie.NANKE
 * @Title: FoolReqest
 * @date 2023/8/13 00:12
 * @description 远程请求类
 */

@Data
public class FoolRequest implements Serializable {

    // 请求全类名
    private String fullClassName;

    // 请求方法名
    private String methodName;

    // 参数列表
    private Object[] args;
}
