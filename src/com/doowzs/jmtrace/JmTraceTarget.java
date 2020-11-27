package com.doowzs.jmtrace;

import org.objectweb.asm.*;

public class JmTraceTarget {
    public int opcode;
    public boolean isWrite;
    public boolean isDynamic;
    public boolean isArray;
    public Type type;
    public int xLoad;
    public int xStore;

    JmTraceTarget(int opcode, boolean isWrite, boolean isDynamic) {
        this.opcode = opcode;
        this.isWrite = isWrite;
        this.isDynamic = isDynamic;
        this.isArray = false;
        this.type = null;
        this.xLoad = Opcodes.NOP;
        this.xStore = Opcodes.NOP;
    }

    JmTraceTarget(int opcode, Type type) {
        this.opcode = opcode;
        this.isWrite = false;
        this.isDynamic = false;
        this.isArray = true;
        this.type = type;
        this.xLoad = Opcodes.NOP;
        this.xStore = Opcodes.NOP;
    }

    JmTraceTarget(int opcode, Type type, int xLoad, int xStore) {
        this.opcode = opcode;
        this.isWrite = true;
        this.isDynamic = false;
        this.isArray = true;
        this.type = type;
        this.xLoad = xLoad;
        this.xStore = xStore;
    }

    public static JmTraceTarget[] targets = new JmTraceTarget[] {
        new JmTraceTarget(Opcodes.GETSTATIC, false, false),
        new JmTraceTarget(Opcodes.PUTSTATIC, true, false),
        new JmTraceTarget(Opcodes.GETFIELD, false, true),
        new JmTraceTarget(Opcodes.PUTFIELD, true, true),
        new JmTraceTarget(Opcodes.AALOAD, Type.getType("[Ljava/lang/Object;")),
        new JmTraceTarget(Opcodes.AASTORE, Type.getType("[Ljava/lang/Object;"), Opcodes.ALOAD, Opcodes.ASTORE),
        new JmTraceTarget(Opcodes.BALOAD, Type.getType("[B")), // byte or boolean
        new JmTraceTarget(Opcodes.BASTORE, Type.getType("[B"), Opcodes.ILOAD, Opcodes.ISTORE),
        new JmTraceTarget(Opcodes.CALOAD, Type.getType("[C")),
        new JmTraceTarget(Opcodes.CASTORE, Type.getType("[C"), Opcodes.ILOAD, Opcodes.ISTORE),
        new JmTraceTarget(Opcodes.DALOAD, Type.getType("[D")),
        new JmTraceTarget(Opcodes.DASTORE, Type.getType("[D"), Opcodes.DLOAD, Opcodes.DSTORE),
        new JmTraceTarget(Opcodes.FALOAD, Type.getType("[F")),
        new JmTraceTarget(Opcodes.FASTORE, Type.getType("[F"), Opcodes.FLOAD, Opcodes.FSTORE),
        new JmTraceTarget(Opcodes.IALOAD, Type.getType("[I")),
        new JmTraceTarget(Opcodes.IASTORE, Type.getType("[I"), Opcodes.ILOAD, Opcodes.ISTORE),
        new JmTraceTarget(Opcodes.LALOAD, Type.getType("[J")),
        new JmTraceTarget(Opcodes.LASTORE, Type.getType("[J"), Opcodes.LLOAD, Opcodes.LSTORE),
        new JmTraceTarget(Opcodes.SALOAD, Type.getType("[S")),
        new JmTraceTarget(Opcodes.SASTORE, Type.getType("[S"), Opcodes.ILOAD, Opcodes.ISTORE)
    };
}