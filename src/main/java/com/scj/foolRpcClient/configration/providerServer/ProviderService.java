package com.scj.foolRpcClient.configration.providerServer;

/**
 * @author suchangjie.NANKE
 * @Title: LocalCache
 * @date 2023/8/21 21:04
 * @description 本地缓存
 */
public interface ProviderService {

    /**
     * 新增响应实例
     * @param name 全类名
     * @param bean 对象
     * @param version 版本
     */
    void put(String name, Object bean, String version);

    /**
     * 获取响应实例
     * @param name 全类名
     * @return Object->Bean
     */
    Object get(String name);
}
