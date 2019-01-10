package org.dracula.test.dubbo.gateway.example.gateway;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.dracula.test.dubbo.gateway.example.TestInterface;
import org.dracula.test.dubbo.gateway.example.TestParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 这个先确保rest直连是成功的
 * @author dk
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = GatewayTest.TmpConfig.class)
public class GatewayTest {

    private static Logger logger = LoggerFactory.getLogger(GatewayTest.class);

    @Autowired
    private TestInterface testInterface;

    @Test
    public void test(){
        TestParam testParam = new TestParam();
        testParam.setText("gxk");
        logger.info(testInterface.sayHello(testParam).getText());
    }

    /**
     * @author dk
     */
    @Configuration
    @DubboComponentScan
    public static class TmpConfig{

        @Bean
        public ApplicationConfig applicationConfig(){
            ApplicationConfig applicationConfig = new ApplicationConfig();
            applicationConfig.setName("test-consumer-by-url");
            return applicationConfig;
        }

        @Bean
        public ReferenceBean referenceBean(){
            ReferenceBean referenceBean = new ReferenceBean();
            referenceBean.setInterface(TestInterface.class);
            referenceBean.setUrl("rest://localhost:8080");
            return referenceBean;
        }

    }

}
