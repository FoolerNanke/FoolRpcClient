package com.scj.foolRpcClient.configration.providerServer;

import lombok.Getter;

import java.util.Collection;

/**
 * @author suchangjie.NANKE
 * @Title: ProviderService
 * @date 2023/8/21 21:04
 * @description 本地缓存
 */
public interface ProviderService {

    @Getter
    final class ProvideBean{
        /**
         * 实例
         */
        private final Object bean;
        /**
         * 版本
         */
        private final String version;
        /**
         * 实例名
         */
        private final String beanName;

        public ProvideBean(Object bean, String version, String beanName) {
            this.bean = bean;
            this.version = version;
            this.beanName = beanName;
        }
    }

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

    /**
     * 获取所有对外提供服务的bean
     * @return bean set
     */
    Collection<ProvideBean> getAllBean();
}
