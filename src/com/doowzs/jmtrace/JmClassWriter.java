package com.doowzs.jmtrace;

import org.objectweb.asm.*;

public class JmClassWriter extends ClassWriter {
    
    public JmClassWriter(ClassReader classReader, int flags) {
        super(classReader, flags);
    }
}