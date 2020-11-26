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
        new JmTraceTarget(Opcodes.AALOAD, true, Type.getType("[Ljava/lang/Object;")),
        new JmTraceTarget(Opcodes.AASTORE, true, Type.getType("[Ljava/lang/Object;"), Opcodes.ALOAD, Opcodes.ASTORE),
        new JmTraceTarget(Opcodes.BALOAD, true, Type.getType("[B")), // byte or boolean
        new JmTraceTarget(Opcodes.BASTORE, true, Type.getType("[B"), Opcodes.ILOAD, Opcodes.ISTORE),
        new JmTraceTarget(Opcodes.CALOAD, true, Type.getType("[C")),
        new JmTraceTarget(Opcodes.CASTORE, true, Type.getType("[C"), Opcodes.ILOAD, Opcodes.ISTORE),
        new JmTraceTarget(Opcodes.DALOAD, true, Type.getType("[D")),
        new JmTraceTarget(Opcodes.DASTORE, true, Type.getType("[D"), Opcodes.DLOAD, Opcodes.DSTORE),
        new JmTraceTarget(Opcodes.FALOAD, true, Type.getType("[F")),
        new JmTraceTarget(Opcodes.FASTORE, true, Type.getType("[F"), Opcodes.FLOAD, Opcodes.FSTORE),
        new JmTraceTarget(Opcodes.IALOAD, true, Type.getType("[I")),
        new JmTraceTarget(Opcodes.IASTORE, true, Type.getType("[I"), Opcodes.ILOAD, Opcodes.ISTORE),
        new JmTraceTarget(Opcodes.LALOAD, true, Type.getType("[J")),
        new JmTraceTarget(Opcodes.LASTORE, true, Type.getType("[J"), Opcodes.LLOAD, Opcodes.LSTORE),
        new JmTraceTarget(Opcodes.SALOAD, true, Type.getType("[S")),
        new JmTraceTarget(Opcodes.SASTORE, true, Type.getType("[S"), Opcodes.ILOAD, Opcodes.ISTORE)
        // TODO: Add opcodes like GETSTATIC, PUTSTATIC
    };
}