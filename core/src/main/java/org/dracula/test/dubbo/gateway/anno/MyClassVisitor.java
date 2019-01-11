package org.dracula.test.dubbo.gateway.anno;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

/**
 * @author dk
 */
public class MyClassVisitor extends ClassVisitor implements Opcodes {

    public MyClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (true) {
            AnnotationVisitor av1 = mv.visitAnnotation("Ljavax/ws/rs/POST;", true);//在request上添加@POST注解
            AnnotationVisitor av2 = mv.visitAnnotation("Ljavax/ws/rs/Path;", true);//同上
            av2.visit("value", "/"+name);
            av2.visitEnd();
            av1.visitEnd();
        }
        return mv;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, boolean arg1) {
        if (name.equals("Lcn/com/*/*/gateway/service/ServiceType;")) {
            return null;//返回null说明，要删掉接口上@ServiceType注解
        }
        return super.visitAnnotation(name, arg1);
    }

}