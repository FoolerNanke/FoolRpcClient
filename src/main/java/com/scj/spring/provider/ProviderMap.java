package com.scj.spring.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
/**
 * @author suchangjie.NANKE
 * @Title: ProviderMap
 * @date 2023/8/20 13:41
 * @description 服务提供方存储容器
 */
public class ProviderMap {

    public static Map<String, Object> map = new ConcurrentHashMap<>();

    public static void put(String name, Object obj){
        map.put(name, obj);
    }

    public static Object get(String name){
        return map.get(name);
    }
}
