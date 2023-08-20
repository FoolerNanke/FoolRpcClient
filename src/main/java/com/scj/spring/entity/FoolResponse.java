package com.scj.spring.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author suchangjie.NANKE
 * @Title: FoolResponse
 * @date 2023/8/19 21:34
 * @description 请求回复
 */
@Data
public class FoolResponse implements Serializable {

    // 回复对象全类名
    private String fullClassName;

    // 参数列表
    private Object data;
}
