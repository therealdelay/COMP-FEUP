#!/bin/bash

jjtree yal2jvm.jjt
javacc yal2jvm.jj
javac *.java
java yal2jvm
