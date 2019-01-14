package org.dracula.test.dubbo.gateway.anno;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.instrument.ClassDefinition;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author dk
 */
public class AsmTest {

    @Test
    public void test() throws Exception {
        byte[] classFile = readClassFile(InterfaceWithoutAnnotation.class.getCanonicalName());
        //
        MyClassFileByteTransformer myClassFileByteTransformer = new MyClassFileByteTransformer();
        byte[] newClassFile = myClassFileByteTransformer.transform(classFile);
        FileOutputStream fos = new FileOutputStream("E:\\tmp\\InterfaceWithoutAnnotation.class");//覆盖当前class文件
        fos.write(newClassFile);
        fos.close();
    }

    private byte[] readClassFile(String className) throws Exception {
        //
        String classFilePath = className.replace(".", "/")+".class";
        InputStream fis = AsmTest.class.getClassLoader().getResourceAsStream(classFilePath);
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
     * mvn packageh后可用jar
     * 启动参数加上-javaagent:target/test-dubbo-gateway-core-1.0-SNAPSHOT.jar
     * 因为ide中有Working Directory为%MODULE_WORKING_DIR%，可如此写相对路径
     *
     * @throws Exception
     */
    @Test
    public void testHotReplace() throws Exception{
        //加载原
        Class clazz = null;
        try {
            clazz = Class.forName("org.dracula.test.dubbo.gateway.anno.InterfaceWithoutAnnotation");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        byte[] previousClass = readClassFile(clazz.getCanonicalName());
        //转换，Instrumentation替换
        byte[] newClass = new MyClassFileByteTransformer().transform(previousClass);
        InstrumentationSavingAgent.getInstrumentation().redefineClasses(new ClassDefinition(clazz, newClass));
        //打印加上的注解
        Annotation[] clazzAnnotations = clazz.getAnnotations();
        Arrays.stream(clazzAnnotations).forEach(annotation -> System.out.println(annotation));
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Arrays.stream(declaredMethods).forEach(
                method -> {
                    System.out.println("方法："+method);
                    Arrays.stream(method.getAnnotations()).forEach(annotation -> System.out.println(annotation));
                }
        );
    }

}