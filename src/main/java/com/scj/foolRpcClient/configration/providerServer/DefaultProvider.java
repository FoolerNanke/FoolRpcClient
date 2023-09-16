package com.scj.foolRpcClient.configration.providerServer;

import com.scj.foolRpcBase.constant.Constant;
import com.scj.foolRpcClient.constant.FRCConstant;
import com.scj.foolRpcBase.handler.in.FoolProtocolDecode;
import com.scj.foolRpcBase.handler.out.FoolProtocolEncode;
import com.scj.foolRpcClient.handler.FoolRemoteReqHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author suchangjie.NANKE
 * @Title: Provider
 * @date 2023/8/20 13:40
 * @description 服务提供方
 */

@Component
public class DefaultProvider implements ProviderService{

    @Autowired
    private FoolRemoteReqHandler foolRemoteReqHandler;

    /**
     * 服务提供方存储对象
     * key:fullClassName
     * value:Bean
     */
    private final Map<String, ProvideBean> ProviderMap;

    public DefaultProvider(){
        new ServerBootstrap()
                .group(FRCConstant.reqEventLoop)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        channel.pipeline().addLast(new FoolProtocolDecode());
                        channel.pipeline().addLast(new FoolProtocolEncode<>());
                        channel.pipeline().addLast(foolRemoteReqHandler);
                    }
                }).bind(Constant.REMOTE_PORT);
        ProviderMap = new ConcurrentHashMap<>();
    }

    @Override
    public void put(String name, Object bean, String version) {
        ProviderMap.put(name, new ProvideBean(bean, version, name));
    }

    @Override
    public Object get(String name) {
        ProvideBean provideBean = ProviderMap.getOrDefault(name, null);
        if (provideBean != null) {
            return provideBean.getBean();
        }
        return null;
    }

    @Override
    public Collection<ProvideBean> getAllBean() {
        return ProviderMap.values();
    }
}
