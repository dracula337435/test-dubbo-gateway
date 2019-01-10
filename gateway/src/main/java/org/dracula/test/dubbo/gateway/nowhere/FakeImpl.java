package org.dracula.test.dubbo.gateway.nowhere;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author dk
 */
public class FakeImpl {

    public static Object getObject(Class objectType, Object ref) {
        return Proxy.newProxyInstance(objectType.getClassLoader(), new Class[]{objectType}, new MyInvocationHandler(ref));
    }

    /**
     * @author dk
     */
    public static class MyInvocationHandler implements InvocationHandler{

        private Object ref;

        public MyInvocationHandler(Object ref) {
            this.ref = ref;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(ref, args);
        }

    }

}
