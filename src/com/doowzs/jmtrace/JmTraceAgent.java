package com.doowzs.jmtrace;

import java.lang.instrument.*;
import java.security.ProtectionDomain;

public class JmTraceAgent {

    private JmTraceAgent() {
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new JmClassFileTransformer());
    }
}