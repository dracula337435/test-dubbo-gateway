# 试验dubbo

异地多活副产品，抽象出来，本质要完成两种dubbo协议的转换

## 失败记录
1. 直接把reference注进service，运行时错误，gateway方报错```com.alibaba.dubbo.common.bytecode.NoSuchMethodException: Method [sayHello] not found.```