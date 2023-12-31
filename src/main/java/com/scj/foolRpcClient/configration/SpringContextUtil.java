package com.scj.foolRpcClient.configration;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author suchangjie.NANKE
 * @Title: SpringContextUtil
 * @date 2023/8/22 21:12
 * @description Spring容器管理工具
 */

@Component
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * 托管容器
     */
    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 根据名称获取bean
     * @param name bean 名称
     * @return bean
     */
    public static Object getBeanByName(String name){
        return applicationContext.getBean(name);
    }

    /**
     * 据类名称获取bean
     * @param clazz 类名
     * @return bean
     */
    public static Object getBeanByClazz(Class<?> clazz){ return applicationContext.getBean(clazz);}
}
