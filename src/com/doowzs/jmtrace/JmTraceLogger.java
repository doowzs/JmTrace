package com.doowzs.jmtrace;

import com.doowzs.jmtrace.JmTraceTarget;

public class JmTraceLogger {

    public static JmTraceTarget getTraceTarget(int opcode) {
        for (int i = 0; i < JmTraceTarget.targets.length; ++i) {
            if (opcode == JmTraceTarget.targets[i].opcode) {
                return JmTraceTarget.targets[i];
            }
        }
        return null;
    }

    public static void printMemoryAccess(int opcode, Object object, String owner, String name) {
        JmTraceTarget target = getTraceTarget(opcode);
        if (target == null) {
            return;
        }

        int threadId = (int)Thread.currentThread().getId();
        long hashCode = (((long)(object == null ? owner.hashCode() : System.identityHashCode(object))) << 32) | ((long)name.hashCode() & 0xffffffffL);
        String typeName = owner.replace("/", ".");
        System.out.print(target.isWrite ? "W" : "R");
        System.out.printf(" %d", threadId);
        System.out.printf(" %016x", hashCode);
        System.out.printf(" %s.%s\n", typeName, name);
    }
    
    public static void printMemoryAccess(int opcode, Object array, int index) {
        JmTraceTarget target = getTraceTarget(opcode);
        if (target == null) {
            return;
        }

        int threadId = (int)Thread.currentThread().getId();
        long hashCode = ((long)System.identityHashCode(array) << 32) | ((long)index & 0xffffffffL);
        String typeName = array.getClass().getComponentType().getCanonicalName();
        System.out.print(target.isWrite ? "W" : "R");
        System.out.printf(" %d", threadId);
        System.out.printf(" %016x", hashCode);
        System.out.printf(" %s[%d]\n", typeName, index);
    }
}