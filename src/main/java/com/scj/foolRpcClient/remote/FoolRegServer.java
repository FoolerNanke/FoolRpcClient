package com.scj.foolRpcClient.remote;

import java.net.InetSocketAddress;

/**
 * @author suchangjie.NANKE
 * @Title: RemoteServer
 * @date 2023/8/12 23:08
 * @description 链接注册中心的接口
 */
public interface FoolRegServer {

    /**
     * @param path 请求方全类名
     * @return 远程服务请求地址
     */
    InetSocketAddress getRpcAddress(String path, String version);

    /**
     * 注册bean到注册中心
     * @param fullClassName 全类名
     * @param version 版本
     */
    void registerClass(String fullClassName, String version);

    /**
     * 重新连接到注册中心后
     * 再次将所有的bean注册上去
     */
    void registerAgain();

    /**
     * 连接到注册中心
     */
    void connect();
}
