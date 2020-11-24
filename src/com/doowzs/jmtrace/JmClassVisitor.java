package com.doowzs.jmtrace;

import org.objectweb.asm.*;

public class JmClassVisitor extends ClassVisitor {
    
    public JmClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM9, classVisitor);
    }

    @Override
    public void visitSource(String source, String debug) {
        System.out.println(source);
        cv.visitSource(source, debug);
    }
}