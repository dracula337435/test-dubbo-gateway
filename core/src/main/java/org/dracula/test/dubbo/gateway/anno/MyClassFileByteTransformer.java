package org.dracula.test.dubbo.gateway.anno;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.Opcodes;

/**
 * @author dk
 */
public class MyClassFileByteTransformer {

    public byte[] transform(byte[] originClassFile){
        ClassReader cr = new ClassReader(originClassFile);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        //在接口上添加注解@Path
        AnnotationVisitor av0 = cw.visitAnnotation("Ljavax/ws/rs/Path;", true);
        av0.visit("value", "/"+cr.getClassName().replace("/", "."));
        av0.visitEnd();

        //在接口上添加注解@Consumes
        AnnotationVisitor av1 = cw.visitAnnotation("Ljavax/ws/rs/Consumes;", true);
        AnnotationVisitor av2 = av1.visitArray("value");
        av2.visit(null, "application/json; charset=UTF-8");
        av2.visitEnd();
        av1.visitEnd();

        //在接口上添加注解@Produces
        AnnotationVisitor av3 = cw.visitAnnotation("Ljavax/ws/rs/Produces;", true);
        AnnotationVisitor av4 = av3.visitArray("value");
        av4.visit(null, "application/json; charset=UTF-8");
        av4.visitEnd();
        av3.visitEnd();

        MyClassVisitor myv = new MyClassVisitor(Opcodes.ASM4, cw);
        cr.accept(myv, 0);

        //最终想要类的字节码
        return cw.toByteArray();
    }

}
