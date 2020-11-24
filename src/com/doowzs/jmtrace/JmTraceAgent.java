package com.doowzs.jmtrace;

import java.lang.instrument.Instrumentation;
import org.objectweb.asm.*;

public class JmTraceAgent {

    private JmTraceAgent() {
    }

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        try {
            ClassPrinter printer = new ClassPrinter();
            ClassReader reader = new ClassReader(inst.getAllLoadedClasses()[0].getName());
            reader.accept(printer, 0);
        } catch (Exception e) {
            throw e;
        }
    }
}