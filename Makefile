ADDFLAGS = -Xlint:unchecked
MYJAVAC = javac $(ADDFLAGS)
all:
	mkdir -p bin
	$(MYJAVAC) -cp lib/java-cup-11a-runtime.jar -d bin src/mini_python/Location.java
	$(MYJAVAC) -cp lib/java-cup-11a-runtime.jar -d bin src/mini_python/annotation/*.java
	$(MYJAVAC) -cp lib/java-cup-11a-runtime.jar:bin/ -d bin src/mini_python/exception/*.java
	javac -cp lib/java-cup-11a-runtime.jar:bin -d bin src/mini_python/*.java
	java -cp lib/java-cup-11a-runtime.jar:bin mini_python.Main --debug test.py

