package com.doowzs.jmtrace;

import java.lang.instrument.*;
import java.security.ProtectionDomain;

public class JmClassFileTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform​(ClassLoader l, String n, Class<?> c, ProtectionDomain d, byte[] b)  throws IllegalClassFormatException {
        JmClassReader cr = new JmClassReader(b);
        JmClassWriter cw = new JmClassWriter(cr, 0);
        JmClassVisitor cv = new JmClassVisitor(cw);
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    @Override
    public byte[] transform​(Module m, ClassLoader l, String n, Class<?> c, ProtectionDomain d, byte[] b)  throws IllegalClassFormatException {
        return transform​(l, n, c, d, b);
    }
}