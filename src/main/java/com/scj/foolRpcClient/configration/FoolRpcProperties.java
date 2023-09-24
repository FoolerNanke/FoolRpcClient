package com.scj.foolRpcClient.configration;

import com.scj.foolRpcBase.constant.Constant;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author suchangjie.NANKE
 * @Title: FoolRpcProperties
 * @date 2023/8/23 00:53
 * @description 配置类
 */

@Data
@Component
@ConfigurationProperties(prefix = "com.scj.fool-rpc.client.app")
public class FoolRpcProperties implements InitializingBean {

    /**
     * 注册中心网关ip
     */
    private String register_ip = "localhost";

    /**
     * 应用名
     */
    private String appName = "app";

    /**
     * 服务名
     */
    private String artifactId;

    /**
     * 包名
     */
    private String groupId;

    /**
     * 版本号
     */
    private String version = "version";

    @Override
    public void afterPropertiesSet() {
        if (appName == null){
            this.appName = artifactId + Constant.GAP_POINT + groupId;
        }
    }
}

