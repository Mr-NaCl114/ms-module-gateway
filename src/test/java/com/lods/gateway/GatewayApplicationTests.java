package com.lods.gateway;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.lods.gateway.infrastructure.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class GatewayApplicationTests {

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private NacosConfigManager nacosConfigManager;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Test
    void contextLoads() {
        System.out.println(this.testConfig);
    }

    @Test
    @Timeout(10)
    void nacosShouldBeReachable() throws NacosException {
        ConfigService configService = nacosConfigManager.getConfigService();
        NamingService namingService = nacosServiceManager.getNamingService();

        assertThat(configService.getServerStatus())
                .as("Nacos Config server status")
                .isEqualTo("UP");
        assertThat(namingService.getServerStatus())
                .as("Nacos Discovery server status")
                .isEqualTo("UP");

        assertThatCode(() -> configService.getConfig(
                "__connectivity_check__", Constants.DEFAULT_GROUP, 3_000))
                .as("Nacos Config API should be reachable")
                .doesNotThrowAnyException();
        assertThat(namingService.getServicesOfServer(1, 1))
                .as("Nacos Discovery API should return a service list")
                .isNotNull();
    }

}
