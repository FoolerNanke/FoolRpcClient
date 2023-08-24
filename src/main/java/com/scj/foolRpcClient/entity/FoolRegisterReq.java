package com.scj.foolRpcClient.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author suchangjie.NANKE
 * @Title: FoolRegisterReq
 * @date 2023/8/24 22:34
 * @description 注册中心注册请求体
 */

@Data
public class FoolRegisterReq implements Serializable {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 时间戳
     */
    private long timeStamp;

    /**
     * 全类名
     */
    private String fullClassName;

    /**
     * 版本
     */
    private String version;
}
