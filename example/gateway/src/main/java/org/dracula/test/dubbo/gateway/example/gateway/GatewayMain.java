package org.dracula.test.dubbo.gateway.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author dk
 */
@SpringBootApplication
@ImportResource("classpath:org/dracula/test/dubbo/gateway/example/gateway/dubbo-gateway.xml")
public class GatewayMain {

    public static void main(String[] args){
        SpringApplication.run(GatewayMain.class, args);
    }

}
