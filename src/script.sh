#!/bin/bash

jjtree yal2jvm.jjt
javacc yal2jvm.jj
javac *.java
#java yal2jvm aval1_err.yal
 java yal2jvm aval2_err.yal
# java yal2jvm aval3_err.yal
# java yal2jvm aval4_err.yal
# java yal2jvm aval5_err.yal
# java yal2jvm aval6_err.yal
# java yal2jvm aval7_err.yal
# java yal2jvm array2_err.yal
# java yal2jvm array4_err.yal