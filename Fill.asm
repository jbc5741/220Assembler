// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

// Grand Loop
(GRANDLOOP)
@24576		// A = Keyboard address
D = M		// Store the current keyboard value in D
@NOKEY		// A = No Key Pressed 
D ; JEQ		// If D (the keyboard value) == 0, jump to No Key Pressed Loop

// If key is pressed, stay here
(KEY)
@color		// A = address of color for screen
M = -1		// RAM[color] = 1
@CONT		// Skip the no key step
0 ; JMP		// jump down to the color loop

(NOKEY)
@color		// A = color address
M = 0		// RAM[color] = 0

(CONT)
@16384		// Start at the beginning of the screen
D = A		// D = the address for the beginning of the screen
@screenpos	// temp variable to hold current screen position
M = D		// RAM[screenpos] = the beginning of the screen

// Loop to color the screen
(COLORLOOP)
@color		// A = color address
D = M		// D = -1 or 0
@screenpos	// A = screen position address
A = M		// A = the screen position
M = D		// RAM[screenpos] = RAM[color]

// Advance screen position
@screenpos	// A = screen position address
M = M + 1	// Increment screen position
D = M		// D = new screen position
@24576		// A = end of screen (+1)
D = A - D	// D = end of screen minus current position (will be zero when finished)
@COLORLOOP	// A = start of COLORLOOP
D ; JGT		// if the end of the screen minus the current position is greater than zero, 
		// keep looping

// End (Loop back to top)
(END)	
@GRANDLOOP	// A = beginning of loop
0 ; JMP		// Infinite Loop