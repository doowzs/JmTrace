package com.doowzs.jmtrace;

public class JmTraceAgent {

    private JmTraceAgent() {
    }

    public static void premain(String agentArgs) {
        System.out.println("Hello, world!");
    }
}