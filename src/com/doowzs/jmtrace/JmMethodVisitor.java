package com.doowzs.jmtrace;

import org.objectweb.asm.*;
import java.util.stream.IntStream;

public class JmMethodVisitor extends MethodVisitor {

    private static int[] targets = new int[] {
        Opcodes.GETSTATIC, Opcodes.PUTSTATIC,
        Opcodes.GETFIELD, Opcodes.PUTFIELD,
        Opcodes.AALOAD, Opcodes.AASTORE,
        Opcodes.BALOAD, Opcodes.BASTORE,
        Opcodes.CALOAD, Opcodes.CASTORE,
        Opcodes.DALOAD, Opcodes.DASTORE,
        Opcodes.FALOAD, Opcodes.FASTORE,
        Opcodes.IALOAD, Opcodes.IASTORE,
        Opcodes.LALOAD, Opcodes.LASTORE,
        Opcodes.SALOAD, Opcodes.SASTORE,
        Opcodes.ALOAD, Opcodes.ASTORE
        // TODO: Add opcodes like xALOAD, xASTORE
    };

    private String owner;
  
    public JmMethodVisitor(MethodVisitor mv, String owner) {
        super(Opcodes.ASM9, mv);
        this.owner = owner;
    }

    @Override
    public void visitInsn(int opcode) {
        mv.visitInsn(opcode);
        System.out.println("visitInsn " + opcode);
        for (int i = 0; i < targets.length; ++i) {
            if (opcode == targets[i]) {
                printMemoryAccessInfos((i % 2 == 0));
                break;
            }
        }
    }

    @Override
    public void visitMaxs(int maxStack, int maxFrame) {
        mv.visitMaxs(maxStack + 5, maxFrame + 10);
    }

    private void printMemoryAccessInfos(boolean isRead) {
        // R 1032 b026324c6904b2a9 cn.edu.nju.ics.Foo.someField

        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(isRead ? "R " : "W ");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);

        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "getId", "()J", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);
    }
} 