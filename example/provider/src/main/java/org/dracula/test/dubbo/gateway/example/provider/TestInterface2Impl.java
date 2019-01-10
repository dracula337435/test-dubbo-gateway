package org.dracula.test.dubbo.gateway.example.provider;

import com.alibaba.dubbo.config.annotation.Service;
import org.dracula.test.dubbo.gateway.example.TestInterface2;
import org.dracula.test.dubbo.gateway.example.TestParam;

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
