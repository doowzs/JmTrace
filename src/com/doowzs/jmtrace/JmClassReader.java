package com.doowzs.jmtrace;

import org.objectweb.asm.*;

public class JmClassReader extends ClassReader {

    public JmClassReader(byte[] b) {
        super(b);
    }
}