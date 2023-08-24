package com.scj.foolRpcClient.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author suchangjie.NANKE
 * @Title: FoolRegisterResp
 * @date 2023/8/24 23:32
 * @description 注册中心注册响应体
 */

@Data
public class FoolRegisterResp implements Serializable {

    /**
     * 请求响应信息
     */
    private String message;

    /**
     * 请求响应码
     */
    private String code;

    /**
     * IP地址
     */
    private String IP;
}
