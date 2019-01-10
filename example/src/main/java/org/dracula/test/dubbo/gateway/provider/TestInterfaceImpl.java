package org.dracula.test.dubbo.gateway.provider;

import com.alibaba.dubbo.config.annotation.Service;
import org.dracula.test.dubbo.gateway.TestInterface;
import org.dracula.test.dubbo.gateway.TestParam;

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
