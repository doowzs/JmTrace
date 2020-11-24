JC = javac
JR = jar
JFLAGS = -g

SRCS = $(wildcard src/com/doowzs/jmtrace/*.java)
OBJS = $(patsubst src/%.java,build/%.class,$(SRCS))

build/com/doowzs/jmtrace/%.class: src/com/doowzs/jmtrace/%.java
	@echo "[JC] $<"
	@$(JC) $(JFLAGS) -d build $<

jmtrace: $(OBJS)
	@echo "[JR] jmtrace"
	@$(JR) cfm jmtrace.jar MANIFEST-MF -C build $(patsubst build/%.class,%.class,$^)

.PHONY: all test clean

all: jmtrace

FILE ?= P1001
test: all
	@mkdir -p build/tests/$(FILE)
	@echo "[JC] tests/$(FILE)/Main.java"
	@$(JC) $(JFLAGS) -d build/tests/$(FILE) tests/$(FILE)/Main.java
	@echo "[JR] build/tests/$(FILE)/Main.class"
	@$(JR) cfe build/tests/$(FILE)/Main.jar Main -C build/tests/$(FILE) Main.class
	@java -javaagent:jmtrace.jar -jar build/tests/$(FILE)/Main.jar

clean:
	@$(RM) -r build jmtrace