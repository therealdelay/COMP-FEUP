.class public aval1
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit locals 10
.limit stack 10
iconst_2
iconst_3
invokestatic aval1/f(II)I

istore_0
iconst_4
bipush 6
invokestatic aval1/f2(II)V


.method public static f(II)I
.limit locals 10
.limit stack 10
iload_0
iload_1
imul
istore_2

.method public static f2(II)V
.limit locals 10
.limit stack 10
iload_0
iload_1
imul
istore_2
