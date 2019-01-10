package org.dracula.test.dubbo.gateway.provider;

import com.alibaba.dubbo.config.annotation.Service;
import org.dracula.test.dubbo.gateway.TestInterface3;
import org.dracula.test.dubbo.gateway.TestParam;

/**
 * @author dk
 */
@Service
public class TestInterface3Impl implements TestInterface3 {

    @Override
    public TestParam sayHello(TestParam testParam) {
        testParam.setText("3 hello "+testParam.getText());
        return testParam;
    }

}
