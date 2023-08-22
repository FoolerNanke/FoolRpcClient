package com.scj.foolRpc.annotation;

/**
 * @author suchangjie.NANKE
 * @Title: ConsumeType
 * @date 2023/8/22 20:47
 * @description 消费方式
 */
public enum ConsumeType {
    /**
     * 同步等待模式
     */
    SYNC,

    /**
     * 通知模式
     * 不等待回复
     */
    NOTICE,

    /**
     * 设置回调函数进行消费
     */
    CALLBACK

}
