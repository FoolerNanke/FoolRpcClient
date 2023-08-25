package com.scj.foolRpcClient.annotation;

import com.scj.foolRpcBase.constant.Constant;

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
    long timeOut() default Constant.TIME_OUT;

    /**
     * 时间单位
     * @return 默认时间单位为毫秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 消费方式
     * @return 默认为同步等待模式
     */
    ConsumeType consumeType() default ConsumeType.SYNC;

    /**
     * 消费方式为回调函数时 需要设置回调函数
     * @return 回调函数路径 格式为 beanName.methodName
     * 注：该 bean 必须被 Spring 托管 FoolRpc 不负责生成 bean
     */
    String CallBackMethod() default "";

    /**
     * 当在注册中心无法找到链接时
     * @return 默认链接为空
     */
    String defaultLink() default "";
}

