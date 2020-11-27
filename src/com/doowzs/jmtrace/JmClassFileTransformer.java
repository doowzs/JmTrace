package com.doowzs.jmtrace;

import java.lang.instrument.*;
import java.security.ProtectionDomain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class JmClassFileTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform​(ClassLoader l, String n, Class<?> c, ProtectionDomain d, byte[] b) throws IllegalClassFormatException {
        if (n.startsWith("java") || n.startsWith("sun") || n.startsWith("jdk") || 
            /* FIXME */ n.startsWith("com/apple") || n.startsWith("apple") || n.startsWith("com/sun") || n.startsWith("com.sun") ) {
            return null;
        } else {
            JmClassReader cr = new JmClassReader(b);
            JmClassWriter cw = new JmClassWriter(cr, ClassWriter.COMPUTE_FRAMES, n);
            JmClassVisitor cv = new JmClassVisitor(cw);
            cr.accept(cv, ClassReader.EXPAND_FRAMES); // expand all frames, see JmMethodVisitor
            return cw.toByteArray();
        }
    }

    @Override
    public byte[] transform​(Module m, ClassLoader l, String n, Class<?> c, ProtectionDomain d, byte[] b) throws IllegalClassFormatException {
        return transform​(l, n, c, d, b);
    }
}