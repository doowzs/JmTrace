package com.doowzs.jmtrace;

import org.objectweb.asm.*;

public class JmByteCodeTarget {
    public int opcode;
    public boolean isRead;
    public int nArgs;
    public int xLoad;
    public int xStore;

    JmByteCodeTarget(int opcode, boolean isRead, int nArgs) {
        this.opcode = opcode;
        this.isRead = isRead;
        this.nArgs = nArgs;
        this.xLoad = Opcodes.NOP;
        this.xStore = Opcodes.NOP;
    }

    JmByteCodeTarget(int opcode, boolean isRead, int nArgs, int xLoad, int xStore) {
        this.opcode = opcode;
        this.isRead = isRead;
        this.nArgs = nArgs;
        this.xLoad = xLoad;
        this.xStore = xStore;
    }
}