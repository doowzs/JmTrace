package com.doowzs.jmtrace;

import java.lang.instrument.*;
import java.security.ProtectionDomain;

public class JmTraceAgent {

    private JmTraceAgent() {
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        // Referred to page 21 of ASM4-Guide
        ClassFileTransformer t = new JmClassFileTransformer();
        inst.addTransformer(t);
    }
}