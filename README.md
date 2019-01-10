# 试验dubbo

异地多活副产品，抽象出来，本质要完成两种dubbo协议的转换

## 失败记录
1. 直接把reference注进service，运行时错误，gateway方报错```com.alibaba.dubbo.common.bytecode.NoSuchMethodException: Method [sayHello] not found.```  
见版本```69d10ea8 on 2019/1/10 at 19:14```
1. 用dubbo泛化实现，运行时错误```java.lang.IllegalArgumentException: argument type mismatch```  
见```generic```项目中的例子，dubbo泛化```GenericService```传入参数，当复杂对象时就成了```Map```，再转换一次的话太麻烦