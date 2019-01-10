package org.dracula.test.dubbo.gateway.gateway;

import com.alibaba.dubbo.rpc.service.GenericException;
import com.alibaba.dubbo.rpc.service.GenericService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author dk
 */
public class GenericTest<T> implements GenericService {

    private Class<T> interfaceClass;

    private T ref;

    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
        Class paramTypes[] = new Class[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            try {
                paramTypes[i] = Class.forName(parameterTypes[i]);
            } catch (ClassNotFoundException e) {
                throw new GenericException(e);
            }
        }
        Method method0;
        try {
            method0 = getInterfaceClass().getMethod(method, paramTypes);
        } catch (NoSuchMethodException e) {
            throw new GenericException(e);
        }
        try {
            return method0.invoke(ref, args);
        } catch (IllegalAccessException e) {
            throw new GenericException(e);
        } catch (InvocationTargetException e) {
            throw new GenericException(e);
        }
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

}
