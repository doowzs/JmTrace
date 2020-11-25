package com.doowzs.jmtrace;

import org.objectweb.asm.*;

public class JmMethodVisitor extends MethodVisitor {
    
    public static class Target {
        public int opcode;
        public boolean isRead;
        public int nArgs;
        public int xLoad;
        public int xStore;

        Target(int opcode, boolean isRead, int nArgs) {
            this.opcode = opcode;
            this.isRead = isRead;
            this.nArgs = nArgs;
            this.xLoad = Opcodes.NOP;
            this.xStore = Opcodes.NOP;
        }

        Target(int opcode, boolean isRead, int nArgs, int xLoad, int xStore) {
            this.opcode = opcode;
            this.isRead = isRead;
            this.nArgs = nArgs;
            this.xLoad = xLoad;
            this.xStore = xStore;
        }

        public static Target[] targets = new Target[] {
            new Target(Opcodes.GETSTATIC, true, 0),
            new Target(Opcodes.PUTSTATIC, false, 0),
            new Target(Opcodes.IALOAD, true, 2),
            new Target(Opcodes.IASTORE, false, 2, Opcodes.ILOAD, Opcodes.ISTORE)
            // TODO: Add opcodes like xALOAD, xASTORE
        };
    }

    private String owner;
    private int localSize;

    public JmMethodVisitor(MethodVisitor mv, String owner) {
        super(Opcodes.ASM9, mv);
        this.owner = owner;
    }

    // We need to get how many local variable boxes are used to save additional data.
    // See https://stackoverflow.com/questions/47674972/getting-the-number-of-local-variables-in-a-method.
    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        localSize = numLocal;
        for (int i = 0; i < numLocal; ++i) {
            if (local[i] == Opcodes.LONG || local[i] == Opcodes.DOUBLE) {
                ++localSize;
            }
        }
        mv.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitInsn(int opcode) {
        System.out.println("visitInsn " + opcode);
        for (int i = 0; i < Target.targets.length; ++i) {
            if (opcode == Target.targets[i].opcode) {
                visitAndPrintMemoryAccessInfos(Target.targets[i]);
                return;
            }
        }
        mv.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxFrame) {
        mv.visitMaxs(maxStack + 5, maxFrame + 10);
    }

    // Format: "R 1032 b026324c6904b2a9 cn.edu.nju.ics.Foo.someField"
    private void visitAndPrintMemoryAccessInfos(Target target) {
        // ... ref (save array and index to local variables)
        if (target.xStore != Opcodes.NOP) {
            mv.visitVarInsn(target.xStore, localSize + 2);
        }
        for (int i = target.nArgs - 1; i >= 0; --i) {
            mv.visitVarInsn(Opcodes.ISTORE, localSize + i);
        }
        for (int i = 0; i < target.nArgs; ++i) {
            mv.visitVarInsn(Opcodes.ILOAD, localSize + i);
        }
        if (target.xLoad != Opcodes.NOP) {
            mv.visitVarInsn(target.xLoad, localSize + 2);
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