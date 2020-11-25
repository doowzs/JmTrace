package com.doowzs.jmtrace;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

public class JmMethodVisitor extends LocalVariablesSorter {
    
    private JmByteCodeTarget[] targets = new JmByteCodeTarget[] {
        new JmByteCodeTarget(Opcodes.IALOAD, true, Type.getType("[I")),
        new JmByteCodeTarget(Opcodes.IASTORE, true, Type.getType("[I"), Opcodes.ILOAD, Opcodes.ISTORE)
        // TODO: Add opcodes like xALOAD, xASTORE
    };

    private String owner;

    public JmMethodVisitor(MethodVisitor mv, int access, String desc, String owner) {
        super(Opcodes.ASM9, access, desc, mv);
        this.owner = owner;
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
        mv.visitMaxs(maxStack + 10, maxFrame + 10);
    }

    // Format: "R 1032 b026324c6904b2a9 cn.edu.nju.ics.Foo.someField"
    private void visitAndPrintMemoryAccessInfos(JmByteCodeTarget target) {
        // ... ref (save array and index to local variables)
        int arrayId = 0, indexId = 0, valueId = 0;
        if (target.isWrite) {
            valueId = newLocal(target.type.getElementType());
            mv.visitVarInsn(target.xStore, valueId);
        }
        if (target.isArray) {
            arrayId = newLocal(target.type);
            indexId = newLocal(Type.INT_TYPE);
            mv.visitVarInsn(Opcodes.ISTORE, indexId);
            mv.visitVarInsn(Opcodes.ASTORE, arrayId);
            mv.visitVarInsn(Opcodes.ALOAD, arrayId);
            mv.visitVarInsn(Opcodes.ILOAD, indexId);
        }
        if (target.isWrite) {
            mv.visitVarInsn(target.xLoad, valueId);
        }
        mv.visitInsn(target.opcode); // consume the instruction

        // ... ->
        // ... System.out string
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(target.isWrite ? "W " : "R ");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
        
        // ... ->
        // ... System.out currentThread
        // ... System.out threadID
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "getId", "()J", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(J)V", false);
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(" ");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
        
        // ... ->
        // ... System.out array[1]
        // ... System.out array 0 object
        // ... System.out array 0 hashCode
        // ... System.out array
        // ... System.out string
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("%016X");
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitTypeInsn(Opcodes.ANEWARRAY, "Ljava/lang/Object;");
        mv.visitInsn(Opcodes.DUP);
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitVarInsn(Opcodes.ALOAD, arrayId);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "identityHashCode", "(Ljava/lang/Object;)I", false);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
        mv.visitInsn(Opcodes.AASTORE);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String)V", false);
    }
} 