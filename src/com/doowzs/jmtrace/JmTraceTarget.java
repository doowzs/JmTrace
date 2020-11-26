package com.doowzs.jmtrace;

import org.objectweb.asm.*;

public class JmTraceTarget {
    public int opcode;
    public boolean isWrite;
    public boolean isArray;
    public Type type;
    public int xLoad;
    public int xStore;

    JmTraceTarget(int opcode, boolean isArray, Type type) {
        this.opcode = opcode;
        this.isWrite = false;
        this.isArray = isArray;
        this.type = type;
        this.xLoad = Opcodes.NOP;
        this.xStore = Opcodes.NOP;
    }

    JmTraceTarget(int opcode, boolean isArray, Type type, int xLoad, int xStore) {
        this.opcode = opcode;
        this.isWrite = true;
        this.isArray = isArray;
        this.type = type;
        this.xLoad = xLoad;
        this.xStore = xStore;
    }

    public static JmTraceTarget[] targets = new JmTraceTarget[] {
        new JmTraceTarget(Opcodes.IALOAD, true, Type.getType("[I")),
        new JmTraceTarget(Opcodes.IASTORE, true, Type.getType("[I"), Opcodes.ILOAD, Opcodes.ISTORE)
        // TODO: Add opcodes like xALOAD, xASTORE
    };
}