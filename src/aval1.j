.class public aval1
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit locals 10
.limit stack 10
iconst_2
iconst_3
invokestatic aval1/f(II)I

istore_0
iload_0
return
.end method


.method public static f(II)I
.limit locals 10
.limit stack 10
iload_0
iload_1
imul
istore_2
ireturn
.end method

method static public <clinit>()V
.limit stack 0
.limit locals 0
return
.end method 
