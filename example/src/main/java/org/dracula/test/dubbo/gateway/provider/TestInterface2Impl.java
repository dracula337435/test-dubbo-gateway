package org.dracula.test.dubbo.gateway.provider;

import com.alibaba.dubbo.config.annotation.Service;
import org.dracula.test.dubbo.gateway.TestInterface2;
import org.dracula.test.dubbo.gateway.TestParam;

/**
 * @author dk
 */
@Service
public class TestInterface2Impl implements TestInterface2 {

    @Override
    public TestParam sayHello(TestParam testParam) {
        testParam.setText("2 hello "+testParam.getText());
        return testParam;
    }

}
