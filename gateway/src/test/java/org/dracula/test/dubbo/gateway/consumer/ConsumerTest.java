package org.dracula.test.dubbo.gateway.consumer;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.dracula.test.dubbo.gateway.TestInterface;
import org.dracula.test.dubbo.gateway.TestInterface2;
import org.dracula.test.dubbo.gateway.TestInterface3;
import org.dracula.test.dubbo.gateway.TestParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author dk
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ConsumerTest.TmpConfig.class)
public class ConsumerTest {

    private static Logger logger = LoggerFactory.getLogger(ConsumerTest.class);

    @Reference
    private TestInterface testInterface;

    @Test
    public void test(){
        TestParam testParam = new TestParam();
        testParam.setText("gxk");
        logger.info(testInterface.sayHello(testParam).getText());
    }

    @Reference
    private TestInterface2 testInterface2;

    @Test
    public void test2(){
        TestParam testParam = new TestParam();
        testParam.setText("gxk");
        logger.info(testInterface2.sayHello(testParam).getText());
    }

    @Reference
    private TestInterface3 testInterface3;

    @Test
    public void test3(){
        TestParam testParam = new TestParam();
        testParam.setText("gxk");
        logger.info(testInterface3.sayHello(testParam).getText());
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
            applicationConfig.setName("test-consumer");
            return applicationConfig;
        }

        @Bean
        public RegistryConfig registryConfig(){
            RegistryConfig registryConfig = new RegistryConfig();
            registryConfig.setAddress("zookeeper://localhost:2181");
            return registryConfig;
        }

    }

}
