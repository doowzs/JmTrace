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
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        for (int i = 0; i < JmTraceTarget.targets.length; ++i) {
            if (opcode == JmTraceTarget.targets[i].opcode) {
                visitAndPrintMemoryAccessInfos(JmTraceTarget.targets[i], owner, name, desc);
                return;
            }
        }
        mv.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitInsn(int opcode) {
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

    private void visitAndPrintMemoryAccessInfos(JmTraceTarget target, String owner, String name, String desc) {
        int objectId = 0;
        int valueId = 0;
        if (target.isDynamic) {
            if (target.isWrite) {
                valueId = newLocal(Type.getType(desc));
                mv.visitVarInsn(Opcodes.ASTORE, valueId);
            }
            objectId = newLocal(Type.getType(owner));
            mv.visitVarInsn(Opcodes.ASTORE, objectId);
            mv.visitVarInsn(Opcodes.ALOAD, objectId);
            if (target.isWrite) {
                mv.visitVarInsn(Opcodes.ALOAD, valueId);
            }
        }
        mv.visitFieldInsn(target.opcode, owner, name, desc); // consume the instruction
        mv.visitIntInsn(Opcodes.SIPUSH, target.opcode);
        if (target.isDynamic) {
            mv.visitVarInsn(Opcodes.ALOAD, objectId);
        } else {
            mv.visitInsn(Opcodes.ACONST_NULL);
        }
        mv.visitLdcInsn(owner);
        mv.visitLdcInsn(name);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(JmTraceLogger.class),
                           "printMemoryAccess", "(ILjava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V", false);
    }

    private void visitAndPrintMemoryAccessInfos(JmTraceTarget target) {
        // save array and index to local variables
        int arrayId = newLocal(target.type);
        int indexId = newLocal(Type.INT_TYPE);
        int valueId = 0;
        if (target.isWrite) {
            valueId = newLocal(target.type.getElementType());
            mv.visitVarInsn(target.xStore, valueId);
        }
        mv.visitVarInsn(Opcodes.ISTORE, indexId);
        mv.visitVarInsn(Opcodes.ASTORE, arrayId);
        mv.visitVarInsn(Opcodes.ALOAD, arrayId);
        mv.visitVarInsn(Opcodes.ILOAD, indexId);
        if (target.isWrite) {
            mv.visitVarInsn(target.xLoad, valueId);
        }
        mv.visitInsn(target.opcode); // consume the instruction
        mv.visitIntInsn(Opcodes.SIPUSH, target.opcode);
        mv.visitVarInsn(Opcodes.ALOAD, arrayId);
        mv.visitVarInsn(Opcodes.ILOAD, indexId);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(JmTraceLogger.class),
                           "printMemoryAccess", "(ILjava/lang/Object;I)V", false);
    }
} 