package org.dracula.test.dubbo.gateway.example.provider.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dk
 */
@RestController
@RequestMapping("/org.dracula.test.dubbo.gateway.example.subpack.TestInterface3")
public class Test3Controller {

    @PostMapping("/sayHello")
    public AnotherTestParam sayHello(@RequestBody AnotherTestParam param){
        param.setText("in web, 3 hello "+param.getText());
        return param;
    }

}
