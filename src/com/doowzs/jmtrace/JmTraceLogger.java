package com.doowzs.jmtrace;

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

        System.out.print(target.isWrite ? "W" : "R");
        System.out.printf(" %d", Thread.currentThread().getId());
        System.out.printf(" %016x", System.identityHashCode(object));
        System.out.printf(" %s.%s\n", owner.replace("/", "."), name);
    }
    
    public static void printMemoryAccess(int opcode, Object array, int index) {
        JmTraceTarget target = getTraceTarget(opcode);
        if (target == null) {
            return;
        }

        System.out.print(target.isWrite ? "W" : "R");
        System.out.printf(" %d", Thread.currentThread().getId());
        System.out.printf(" %016x", ((long)System.identityHashCode(array) << 32) | index);
        System.out.printf(" %s[%d]\n", array.getClass().getComponentType().getCanonicalName(), index);
    }
}