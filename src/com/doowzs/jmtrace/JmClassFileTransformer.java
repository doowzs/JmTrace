package com.doowzs.jmtrace;

import java.lang.instrument.*;
import java.security.ProtectionDomain;
import org.objectweb.asm.ClassReader;

public class JmClassFileTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform​(ClassLoader l, String n, Class<?> c, ProtectionDomain d, byte[] b)  throws IllegalClassFormatException {
        JmClassReader cr = new JmClassReader(b);
        JmClassWriter cw = new JmClassWriter(cr, 0);
        JmClassVisitor cv = new JmClassVisitor(cw);
        cr.accept(cv, ClassReader.EXPAND_FRAMES); // expand all frames, see JmMethodVisitor
        System.out.println("Complete!");
        return cw.toByteArray();
    }

    @Override
    public byte[] transform​(Module m, ClassLoader l, String n, Class<?> c, ProtectionDomain d, byte[] b)  throws IllegalClassFormatException {
        return transform​(l, n, c, d, b);
    }
}