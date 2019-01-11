package org.dracula.test.dubbo.gateway.example.provider;

import com.alibaba.dubbo.config.annotation.Service;
import org.dracula.test.dubbo.gateway.example.subpack.TestInterface3;
import org.dracula.test.dubbo.gateway.example.TestParam;

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
