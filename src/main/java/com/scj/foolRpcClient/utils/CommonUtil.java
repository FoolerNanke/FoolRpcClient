package com.scj.foolRpcClient.utils;

/**
 * @author: suchangjie.NANKE
 * @Title: CommonUtil
 * @date: 2023/9/28
 * @description: //TODO
 */
public class CommonUtil {

    /**
     * 拼接类连接名
     * @param base 基础类名
     * @param uniqueName 别名
     * @return 基础类名.别名
     */
    public static String buildName(String base, String uniqueName){
        if (uniqueName.isEmpty()){
            return base;
        }
        else return base + "." + uniqueName;
    }
}
