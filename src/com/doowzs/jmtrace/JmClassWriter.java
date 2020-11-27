package com.doowzs.jmtrace;

import org.objectweb.asm.*;

public class JmClassWriter extends ClassWriter {
    
    private String name;

    public JmClassWriter(ClassReader classReader, int flags, String name) {
        super(classReader, flags);
        this.name = name;
    }

    // We cannot calculate the super class of a currently constructing class.
    // For example, method Foo in class A creates an instance of A, then we must
    // override the below method or calculating frame of Foo will require loading
    // class A which is impossible because class A is currently being loaded.
    // See https://gitlab.ow2.org/asm/asm/-/blob/master/asm/src/main/java/org/objectweb/asm/ClassWriter.java#L1002
    @Override
    protected String getCommonSuperClass(final String type1, final String type2) {
        if (type1.equals(name) || type2.equals(name)) {
            return "java/lang/Object";
        } else {
            return super.getCommonSuperClass(type1, type2);
        }
    }
}