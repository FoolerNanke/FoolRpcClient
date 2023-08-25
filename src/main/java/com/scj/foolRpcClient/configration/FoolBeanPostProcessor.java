package com.scj.foolRpcClient.configration;

import com.scj.foolRpcClient.annotation.FoolRpcConsumer;
import com.scj.foolRpcClient.annotation.FoolRpcProvider;
import com.scj.foolRpcClient.constant.LocalCache;
import com.scj.foolRpcBase.entity.FoolResponse;
import com.scj.foolRpcClient.configration.comsumerProxy.AbstractFoolProxy;
import com.scj.foolRpcBase.exception.ExceptionEnum;
import com.scj.foolRpcBase.exception.FoolException;
import com.scj.foolRpcClient.remote.FoolRegServer;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.annotation.Configuration;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

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

@Slf4j
@Configuration
public class FoolBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private AbstractFoolProxy abstractFoolProxy;

    @Autowired
    private FoolRegServer foolRegServer;

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
                // 将类信息注册到注册中心
                foolRegServer.registerClass(inter.getName()
                        , inter.getPackage().getImplementationVersion());
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
                    switch (annotation.consumeType()) {
                        case SYNC:
                            // 同步等待模式
                            FoolResponse foolResponse = (FoolResponse) intercept
                                    .get(annotation.timeOut(), annotation.timeUnit());
                            return foolResponse.getData();

                        case CALLBACK:
                            // 异步回调模式 回调函数消费
                            String callBackMethod = annotation.CallBackMethod();
                            if (!callBackMethod.contains(".")) {
                                // 未填写回调地址
                                log.error("回调函数填写异常");
                                throw new FoolException(ExceptionEnum.CALL_BACK_METHOD_ERROR);
                            }

                            // 设置回调函数
                            intercept.addListener(new FutureListener<Object>() {
                                @Override
                                public void operationComplete(Future<Object> future) throws ExecutionException, InterruptedException {
                                    FoolResponse foolResponse = (FoolResponse) future.get();
                                    String[] split = callBackMethod.split("\\.");
                                    Object bean = SpringContextUtil.getBeanByName(split[0]);
                                    try {
                                        Method me = bean.getClass().getMethod(split[1], FoolResponse.class);
                                        me.setAccessible(true);
                                        me.invoke(bean, foolResponse);
                                    } catch (NoSuchMethodException
                                             | IllegalAccessException
                                             | InvocationTargetException e) {
                                        // 方法不存在或者方法无法访问
                                        log.error("回调函数填写异常");
                                        throw new FoolException(ExceptionEnum.CALL_BACK_METHOD_ERROR, e);
                                    }
                                }
                            });
                            return null;
                        default:
                            return null;
                    }
                });
                declaredField.set(bean, enhancer.create());
            } catch (IllegalAccessException e) {
                log.error("Bean增强异常");
                throw new FoolException(ExceptionEnum.ILLEGAL_ACCESS, e);
            }
        }
        return bean;
    }
}
