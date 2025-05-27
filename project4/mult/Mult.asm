// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
// The algorithm is based on repetitive addition.

//initialize 2 to 0
@2
M=0
//check if R1 or R0 is zero
@0
D=M
@ZERO
D;JEQ

@1
D=M
@ZERO
D;JEQ

//initialize i=1
@i
M=1
//if i>R0 stop
(LOOP)
@i
D=M
@0
D=D-M
@END
D;JGT
//add R1 to R2
@1
D=M
@2
M=D+M
//i=i+1
@i
M=M+1
@LOOP
0;JMP

(ZERO)
@2
M=0
0;JMP

(END)
@END
0;JMP













