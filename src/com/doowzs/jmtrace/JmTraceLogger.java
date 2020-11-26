package com.doowzs.jmtrace;

public class JmTraceLogger {
    
    public static void printMemoryAccess(int opcode, Object array, int index) {
        JmTraceTarget target = null;
        for (int i = 0; i < JmTraceTarget.targets.length; ++i) {
            if (opcode == JmTraceTarget.targets[i].opcode) {
                target = JmTraceTarget.targets[i];
                break;
            }
        }
        if (target == null) {
            return;
        }

        System.out.print(target.isWrite ? "W" : "R");
        System.out.printf(" %d", Thread.currentThread().getId());
        System.out.printf(" %016x", System.identityHashCode(array));
        System.out.printf(" %s[%d]\n", array.getClass().getComponentType().getCanonicalName(), index);
    }
}