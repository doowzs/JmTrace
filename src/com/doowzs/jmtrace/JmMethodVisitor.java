package com.doowzs.jmtrace;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

public class JmMethodVisitor extends LocalVariablesSorter {

    private String owner;

    public JmMethodVisitor(MethodVisitor mv, int access, String desc, String owner) {
        super(Opcodes.ASM9, access, desc, mv);
        this.owner = owner;
    }

    @Override
    public void visitInsn(int opcode) {
        //System.out.println("visitInsn " + opcode);
        for (int i = 0; i < JmTraceTarget.targets.length; ++i) {
            if (opcode == JmTraceTarget.targets[i].opcode) {
                visitAndPrintMemoryAccessInfos(JmTraceTarget.targets[i]);
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
    private void visitAndPrintMemoryAccessInfos(JmTraceTarget target) {
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
        mv.visitIntInsn(Opcodes.BIPUSH, target.opcode);
        if (target.isArray) {
            mv.visitVarInsn(Opcodes.ALOAD, arrayId);
            mv.visitVarInsn(Opcodes.ILOAD, indexId);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(JmTraceLogger.class),
                               "printMemoryAccess", "(ILjava/lang/Object;I)V", false);
        }
    }
} 