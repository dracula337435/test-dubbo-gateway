package org.dracula.test.dubbo.gateway.anno;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;

/**
 * @author dk
 */
public class MyAssist {

    /**
     *
     * @param className
     * @return
     * @throws Exception
     */
    public static byte[] readClassFile(String className) throws Exception {
        return readClassFile(MyAssist.class.getClassLoader(), className);
    }

    /**
     *
     * @param classLoader
     * @param className
     * @return
     * @throws Exception
     */
    public static byte[] readClassFile(ClassLoader classLoader, String className) throws Exception {
        //
        String classFilePath = className.replace(".", "/")+".class";
        InputStream fis = classLoader.getResourceAsStream(classFilePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //
        byte[] buffer = new byte[4096];
        int len = -1;
        while((len = fis.read(buffer)) != -1){
            byteArrayOutputStream.write(buffer, 0, len);
        }
        byte[] classFile = byteArrayOutputStream.toByteArray();
        return classFile;
    }

    /**
     *
     * @param name
     * @return
     */
    public static ClassDefinition transform(String name) throws Exception{
        //加载原
        Class clazz = null;
        try {
            clazz = Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        byte[] previousClass = MyAssist.readClassFile(clazz.getCanonicalName());
        //转换，Instrumentation替换
        byte[] newClass = new MyClassFileByteTransformer().transform(previousClass);
        return new ClassDefinition(clazz, newClass);
    }

}
