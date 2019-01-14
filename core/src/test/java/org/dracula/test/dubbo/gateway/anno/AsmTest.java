package org.dracula.test.dubbo.gateway.anno;

import org.junit.Test;

import java.io.FileOutputStream;
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
        ClassDefinition classDefinition = MyAssist.transform("org.dracula.test.dubbo.gateway.anno.InterfaceWithoutAnnotation");
        byte[] newClassFile = classDefinition.getDefinitionClassFile();
        FileOutputStream fos = new FileOutputStream("E:\\tmp\\InterfaceWithoutAnnotation.class");//覆盖当前class文件
        fos.write(newClassFile);
        fos.close();
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
        ClassDefinition classDefinition = MyAssist.transform("org.dracula.test.dubbo.gateway.anno.InterfaceWithoutAnnotation");
        InstrumentationSavingAgent.getInstrumentation().redefineClasses(classDefinition);
        Class clazz = classDefinition.getDefinitionClass();
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