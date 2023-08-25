package com.scj.foolRpcClient.configration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author suchangjie.NANKE
 * @Title: MyProperties
 * @date 2023/8/23 00:53
 * @description 配置类
 */

@Data
@Component
@ConfigurationProperties(prefix = "com.scj.foolrpc.client")
public class FoolRpcProperties {

    private String value;

    private String register_ip;
}

