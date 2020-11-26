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
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(JmTraceLogger.class), "printMemoryAccess", "()V", false);
    }
} 