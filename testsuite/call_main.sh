#!/bin/bash
 
jjtree -OUTPUT_DIRECTORY=../src/yal ../src/yal/yal2jvm.jjt
javacc -OUTPUT_DIRECTORY=../src/yal ../src/yal/yal2jvm.jj
mkdir -p ../bin
javac -Xlint:unchecked -d ../bin ../src/yal/*.java
cd ../bin
java yal.yal2jvm testsuite/call-main.yal
cd ../testsuite