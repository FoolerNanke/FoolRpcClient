package com.scj.spring.configration;

import com.scj.spring.annotation.FoolRpcConsumer;
import com.scj.spring.annotation.FoolRpcProvider;
import com.scj.spring.constant.LocalCache;
import com.scj.spring.entity.FoolResponse;
import com.scj.spring.configration.comsumerProxy.AbstractFoolProxy;
import io.netty.util.concurrent.Promise;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.annotation.Configuration;
import java.lang.reflect.Field;

/**
 * @author suchangjie.NANKE
 * @Title: FoolBeanPostProcessor
 * @date 2023/8/12 20:07
 * @description
 * 自定义 bean 前置后置构造组件
 * 当 SpringBoot 容器在构造 bean 时候
 * 会调用 “前置函数” 和 “后置函数”
 * 在 “后置函数” 中通过识别 该类的属性上的 @FoolRpcConsumer 注解
 * 来对属性进行增强 使得其函数可以完成远程调用
 */
@Configuration
public class FoolBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private AbstractFoolProxy abstractFoolProxy;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        /*
         provider 扫描
         */
        FoolRpcProvider provider = clazz.getAnnotation(FoolRpcProvider.class);
        if (provider != null){
            Class<?>[] interfaces = clazz.getInterfaces();
            // 对所有继承的接口进行映射扩展
            for (Class<?> inter : interfaces) {
                /*
                 覆盖式
                 如果一个接口存在多个实现 且均被 @FoolRpcProvider 修饰
                 则只会存储最后一个被扫描到的Bean
                 */
                LocalCache.put(inter.getName(), bean);
            }
        }
        /*
         consumer 增强
         */
        for (Field declaredField : clazz.getDeclaredFields()) {
            // 判断注解是否存在
            FoolRpcConsumer annotation = declaredField.getAnnotation(FoolRpcConsumer.class);
            if (annotation == null){
                continue;
            }
            declaredField.setAccessible(true);
            // 方法增强
            try {
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(declaredField.getType());
                enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
                    // 类型强转
                    Promise<?> intercept = (Promise<?>) abstractFoolProxy
                            .intercept(o, method, objects, methodProxy);
                    // 获取响应结果
                    // 等待时间取自 消费注解
                    // TODO 后续新增拓展
                    // TODO 该接口的请求结果可不消费 或者异步响应消费
                    FoolResponse foolResponse = (FoolResponse) intercept
                            .get(annotation.timeOut(), annotation.timeUnit());
                    return foolResponse.getData();
                });
                declaredField.set(bean, enhancer.create());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return bean;
    }
}
