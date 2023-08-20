package com.scj.spring.remote;

import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author suchangjie.NANKE
 * @Title: RemoteServerImpl
 * @date 2023/8/19 22:19
 * @description 远程地址获得器
 */

@Component
public class RemoteServerImpl implements RemoteServer{
    @Override
    public InetSocketAddress getRpcAddress(String path) {
        return new InetSocketAddress("localhost", 5001);
    }
}
