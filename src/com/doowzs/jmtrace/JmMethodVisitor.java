package com.doowzs.jmtrace;

import org.objectweb.asm.*;
import java.util.stream.IntStream;

public class JmMethodVisitor extends MethodVisitor {

    private static int[] targets = new int[] {
        Opcodes.GETSTATIC, Opcodes.PUTSTATIC,
        Opcodes.GETFIELD, Opcodes.PUTFIELD,
        Opcodes.AALOAD, Opcodes.AASTORE,
        Opcodes.ALOAD, Opcodes.ASTORE
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
        if (IntStream.of(targets).anyMatch(x -> x == opcode)) {
            System.out.println(opcode);
        }
    }
}