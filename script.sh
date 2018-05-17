#!/bin/bash

mkdir -p bin
jjtree -OUTPUT_DIRECTORY=src/yal src/yal/yal2jvm.jjt
javacc -OUTPUT_DIRECTORY=src/yal src/yal/yal2jvm.jj
javac -d bin src/yal/*.java
cd bin
java yal.yal2jvm $1
cd ..