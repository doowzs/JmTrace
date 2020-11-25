JC = javac
JR = jar
JFLAGS = -g -cp "src:libs/*"

SRCS = $(wildcard src/com/doowzs/jmtrace/*.java)
OBJS = $(patsubst src/%.java,build/%.class,$(SRCS))

init:
	@if [[ ! -d "libs" || ! -f "libs/asm.jar" ]]; then \
		mkdir -p libs; echo "Downloading ASM from Maven center..."; \
		curl -# https://repo1.maven.org/maven2/org/ow2/asm/asm/9.0/asm-9.0.jar > libs/asm.jar; \
		cd libs && jar xf asm.jar; \
	fi

build/com/doowzs/jmtrace/%.class: src/com/doowzs/jmtrace/%.java
	@echo "[JC] $<"
	@$(JC) $(JFLAGS) -d build $<

jmtrace: $(OBJS)
	@echo "[JR] jmtrace"
	@$(JR) cfm jmtrace.jar src/resources/META-INF/MANIFEST.MF \
		-C libs org $(patsubst build/%.class,-C build %.class,$^)

.PHONY: all test clean
.DEFAULT_GOAL=all

all: init jmtrace

FILE ?= P1001
test: all
	@mkdir -p build/tests/$(FILE)
	@echo "[JC] tests/$(FILE)/Main.java"
	@$(JC) $(JFLAGS) -d build/tests/$(FILE) tests/$(FILE)/Main.java
	@echo "[JR] build/tests/$(FILE)/Main.class"
	@$(JR) cfe build/tests/$(FILE)/Main.jar Main -C build/tests/$(FILE) Main.class
	@java -cp "src:libs/*" -javaagent:jmtrace.jar -jar build/tests/$(FILE)/Main.jar

clean:
	@$(RM) -r libs build *.jar
