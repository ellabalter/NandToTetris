// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.

//// Replace this comment with your code.
@16384
D=A
@screen_ptr
M=D

(LOOP)
@24576
D=M
@BLACK
D;JNE
@WHITE
0;JMP

(BLACK)
@16384
D=A
@screen_ptr
M=D

(BLACK_LOOP)
@24576
D=M
@WHITE
D;JEQ
@screen_ptr
A=M
M=-1
@screen_ptr
M=M+1
@24576
D=A
@screen_ptr
D=D-M
@BLACK_LOOP
D;JGT
@LOOP
0;JMP



(WHITE)
@16384
D=A
@screen_ptr
M=D

(WHITE_LOOP)
@24576
D=M
@BLACK
D;JNE

@screen_ptr
A=M
M=0
@screen_ptr
M=M+1
@24576
D=A
@screen_ptr
D=D-M
@WHITE_LOOP
D;JGT
@LOOP
0;JMP




