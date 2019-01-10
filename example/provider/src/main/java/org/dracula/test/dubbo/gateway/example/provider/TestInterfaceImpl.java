package org.dracula.test.dubbo.gateway.example.provider;

import com.alibaba.dubbo.config.annotation.Service;
import org.dracula.test.dubbo.gateway.example.TestInterface;
import org.dracula.test.dubbo.gateway.example.TestParam;

/**
 * @author dk
 */
@Service
public class TestInterfaceImpl implements TestInterface {

    @Override
    public TestParam sayHello(TestParam testParam) {
        testParam.setText("hello "+testParam.getText());
        return testParam;
    }

    @Override
    public TestParam sayHello2(TestParam testParam) {
        testParam.setText("hello 2 "+testParam.getText());
        return testParam;
    }

}
