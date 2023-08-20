package com.scj.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author suchangjie.NANKE
 * @Title: FoolRpcConsumer
 * @date 2023/8/12 20:04
 * @description
 * 标志该属性为 FoolRpc 的消费方
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FoolRpcConsumer {

    /**
     * 可扩展数据
     * @return value
     */
    String value() default "fool.NANKE";

    /**
     * 超时时间
     * @return 默认超时时间数据为 3000
     */
    long timeOut() default 3000;

    /**
     * 时间单位
     * @return 默认时间单位为毫秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 当在注册中心无法找到链接时
     * @return 默认链接为空
     */
    String defaultLink() default "";
}

