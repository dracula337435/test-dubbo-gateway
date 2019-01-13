package org.dracula.test.dubbo.gateway.anno;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author dk
 */
public class AsmTest {

    @Test
    public void test() throws Exception {
        //
        InputStream fis = AsmTest.class.getClassLoader().getResourceAsStream("org\\dracula\\test\\dubbo\\gateway\\anno\\InterfaceWithoutAnnotation.class");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //
        byte[] buffer = new byte[4096];
        int len = -1;
        while((len = fis.read(buffer)) != -1){
            byteArrayOutputStream.write(buffer, 0, len);
        }
        byte[] classFile = byteArrayOutputStream.toByteArray();
        //
        MyClassFileByteTransformer myClassFileByteTransformer = new MyClassFileByteTransformer();
        byte[] newClassFile = myClassFileByteTransformer.transform(classFile);
        FileOutputStream fos = new FileOutputStream("E:\\tmp\\InterfaceWithoutAnnotation.class");//覆盖当前class文件
        fos.write(newClassFile);
        fos.close();
    }

}