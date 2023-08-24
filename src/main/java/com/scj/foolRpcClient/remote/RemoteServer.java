package com.scj.foolRpcClient.remote;

import java.net.InetSocketAddress;

/**
 * @author suchangjie.NANKE
 * @Title: RemoteServer
 * @date 2023/8/12 23:08
 * @description 链接注册中心的接口
 */
public interface RemoteServer {

    /**
     * @param path 请求方全类名
     * @return 远程服务请求地址
     */
    InetSocketAddress getRpcAddress(String path, String version);
}
