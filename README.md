# 试验dubbo

异地多活副产品，抽象出来，本质要完成两种dubbo协议的转换

## example说明
1. example分为interface，provider，gateway，consumer模块  
1. 启动```ProviderMain```，发布了TestInterface...这3个服务，再启动```GatewayMain```，测试类```ConsumerTest```
1. gateway是基于包自动扫描的，```org.dracula.test.dubbo.gateway.example```包下有TestInterface，TestInterface2，再一层subpack下有TestIntreface3  
在```dubbo-gateway.xml```中修改```MyScannerConfigurer```的```basePackage```可见到效果  
若```basePackage```为```org.dracula.test.dubbo.gateway.example```，测试类调用3个接口均成功  
若```basePackage```为```org.dracula.test.dubbo.gateway.example.subpack```，测试类调用3个接口仅成功位于subpack的```TestInterface3```  
1. controller提供者+dubbo消费者rest直连，成功  
dubbo提供者rest+RestTemplate消费者，成功  
意味着dubbo-rest可以做到跨架构，甚至跨语言。gateway价值更加凸显

## 失败记录
1. 直接把reference注进service，运行时错误，gateway方报错```com.alibaba.dubbo.common.bytecode.NoSuchMethodException: Method [sayHello] not found.```  
见版本```69d10ea8 on 2019/1/10 at 19:14```
1. 用dubbo泛化实现，运行时错误```java.lang.IllegalArgumentException: argument type mismatch```  
见```generic```项目中的例子，dubbo泛化```GenericService```传入参数，当复杂对象时就成了```Map```，再转换一次的话太麻烦  
见版本```24a91938 on 2019/1/10 at 19:18```