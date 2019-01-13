package org.dracula.test.dubbo.gateway.example.consumer;

import org.dracula.test.dubbo.gateway.example.provider.web.AnotherTestParam;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author dk
 */
public class ConsumerWebTest {

    private static Logger logger = LoggerFactory.getLogger(ConsumerWebTest.class);

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void test(){
        AnotherTestParam param = new AnotherTestParam();
        param.setText("gxk");
        AnotherTestParam response = restTemplate.postForObject("http://localhost:8080/TestInterface/sayHello", param, AnotherTestParam.class);
        logger.info(response.getText());
    }

}
