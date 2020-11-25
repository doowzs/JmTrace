package com.doowzs.jmtrace;

import org.objectweb.asm.*;

public class JmByteCodeTarget {
    public int opcode;
    public boolean isWrite;
    public boolean isArray;
    public Type type;
    public int xLoad;
    public int xStore;

    JmByteCodeTarget(int opcode, boolean isArray) {
        this.opcode = opcode;
        this.isWrite = false;
        this.isArray = isArray;
        this.xLoad = Opcodes.NOP;
        this.xStore = Opcodes.NOP;
    }

    JmByteCodeTarget(int opcode, boolean isArray, Type type, int xLoad, int xStore) {
        this.opcode = opcode;
        this.isWrite = true;
        this.isArray = isArray;
        this.xLoad = xLoad;
        this.xStore = xStore;
    }
}