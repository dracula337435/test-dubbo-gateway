package org.dracula.test.dubbo.gateway.example.provider.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dk
 */
@RestController
@RequestMapping("/org.dracula.test.dubbo.gateway.example.TestInterface")
public class TestController {

    @PostMapping("/sayHello")
    public AnotherTestParam sayHello(@RequestBody AnotherTestParam param){
        param.setText("in web, hello "+param.getText());
        return param;
    }

    @PostMapping("/sayHello2")
    public AnotherTestParam sayHello2(@RequestBody AnotherTestParam param){
        param.setText("in web, hello 2 "+param.getText());
        return param;
    }

}
