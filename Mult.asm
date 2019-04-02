// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

@2		// A = 2
M = 0		// RAM[2] = 0 (base case)
@1		// A = 1
D = M		// D = RAM[1]
@temp		// A = temp
M = D		// RAM[temp] = RAM[1]

// while Loop
(BEGINWHILE)
@temp		// A = temp
D = M		// D = RAM[temp]
@ENDWHILE	// A = endWhile
D ; JLE		// if RAM[temp] <= 0, go to the end of the loop
@0		// A = 0
D = M		// D = RAM[0]
@2		// A = 2
M = M + D	// Add one of RAM[0] to RAM[2]
@temp		// A = temp
M = M - 1	// Subtract 1 from RAM[temp]
@BEGINWHILE// A = beginWhile
0 ; JMP		// Jump to the beginning of the loop

// after multiplying
(ENDWHILE)

// Infinite Loop
(END)
@END		// A = END
0 ; JMP		// Infinite loop
