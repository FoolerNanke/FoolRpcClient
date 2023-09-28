package com.scj.foolRpcClient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author suchangjie.NANKE
 * @Title: FoolRpcProvider
 * @date 2023/8/12 20:05
 * @description
 * 标志类为 FoolRpc 的提供方
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FoolRpcProvider {

    /**
     * 可扩展字段
     * @return value
     */
    String uniqueName() default ".";
}
