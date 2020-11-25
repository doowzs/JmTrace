package com.doowzs.jmtrace;

import org.objectweb.asm.*;
import java.util.stream.IntStream;

public class JmMethodVisitor extends MethodVisitor {
    
    static class Target {
        public int opcode;
        public int nArgs;
        public int xLoad;
        public int xStore;
        public boolean isRead;

        Target(int opcode, int nArgs) {
            this.opcode = opcode;
            this.nArgs = nArgs;
            this.xLoad = this.xStore = Opcodes.NOP;
        }

        Target(int opcode, int nArgs, int xLoad, int xStore) {
            this.opcode = opcode;
            this.nArgs = nArgs;
            this.xLoad = xLoad;
            this.xStore = xStore;
        }
    }

    private static Target[] targets = new Target[] {
        new Target(Opcodes.GETSTATIC, 0),
        new Target(Opcodes.PUTSTATIC, 0),
        new Target(Opcodes.IALOAD, 2),
        new Target(Opcodes.IASTORE, 2, Opcodes.ILOAD, Opcodes.ISTORE)
        // TODO: Add opcodes like xALOAD, xASTORE
    };

    private String owner;
    private int nStack;

    public JmMethodVisitor(MethodVisitor mv, String owner) {
        super(Opcodes.ASM9, mv);
        this.owner = owner;
    }

    // We need to get how many local variable boxes are used to save additional data.
    // See https://stackoverflow.com/questions/47674972/getting-the-number-of-local-variables-in-a-method.
    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        nStack = nLocal;
        for (int i = 0; i < nLocal; ++i) {
            if (local[i] == Opcodes.LONG || local[i] == Opcodes.DOUBLE) {
                ++nStack;
            }
        }
    }

    @Override
    public void visitInsn(int opcode) {
        System.out.println("visitInsn " + opcode);
        for (int i = 0; i < targets.length; ++i) {
            if (opcode == targets[i].opcode) {
                visitAndPrintMemoryAccessInfos(targets[i]);
                return;
            }
        }
        mv.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxFrame) {
        mv.visitMaxs(maxStack + 20, maxFrame + 20);
    }

    // Format: "R 1032 b026324c6904b2a9 cn.edu.nju.ics.Foo.someField"
    private void visitAndPrintMemoryAccessInfos(Target target) {
        // ... ref (save array and index to local variables)
        if (target.xStore != Opcodes.NOP) {
            mv.visitVarInsn(target.xStore, nStack + 2);
        }
        for (int i = target.nArgs - 1; i >= 0; --i) {
            mv.visitVarInsn(Opcodes.ISTORE, nStack + i);
        }
        for (int i = 0; i < target.nArgs; ++i) {
            mv.visitVarInsn(Opcodes.ILOAD, nStack + i);
        }
        if (target.xLoad != Opcodes.NOP) {
            mv.visitVarInsn(target.xLoad, nStack + 2);
        }
        mv.visitInsn(target.opcode); // consume the instruction

        // ... ref ->
        // ... ref System.out string
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(target.isRead ? "R " : "W ");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
        
        // ... ref ->
        // ... ref System.out currentThread
        // ... ref System.out threadID
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "getId", "()J", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);
    }
} 