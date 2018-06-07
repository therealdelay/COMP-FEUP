#!/bin/bash
 
jjtree -OUTPUT_DIRECTORY=src/yal src/yal/yal2jvm.jjt
javacc -OUTPUT_DIRECTORY=src/yal src/yal/yal2jvm.jj
mkdir -p bin/generatedFiles
cp libs/io.class bin/generatedFiles
javac -Xlint:unchecked -d bin src/yal/*.java
cd bin
jar -cevf yal/yal2jvm yal2jvm.jar yal/*.class
cd ..