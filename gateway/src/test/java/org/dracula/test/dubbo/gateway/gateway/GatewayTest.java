package org.dracula.test.dubbo.gateway.gateway;

import org.dracula.test.dubbo.gateway.TestInterface;
import org.dracula.test.dubbo.gateway.TestParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 这个先确保rest直连是成功的
 * @author dk
 */
@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:org/dracula/test/dubbo/gateway/gateway/dubbo-gateway-direct.xml")
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

}
