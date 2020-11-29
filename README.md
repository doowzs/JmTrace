# JmTrace

Java (11+) shared memory access tracer.

```
Usage: java -javaagent:jmtrace.jar ...
```

## Building

Requires: JDK 11+, e.g. AdoptOpenJDK 11+.

All dependencies will be downloaded from Maven center, a good Internet connection is needed. If the download is aborted, run `make clean` before another try.

```
$ make
Downloading ASM from Maven center...
################################################ 100.0%
################################################ 100.0%
[JC] src/com/doowzs/jmtrace/JmClassFileTransformer.java
[JC] src/com/doowzs/jmtrace/JmTraceAgent.java
[JR] jmtrace
$ java -javaagent:jmtrace.jar ...
```