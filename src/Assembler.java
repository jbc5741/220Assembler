/**
 * Assembler.java Initializes I/O files and handles errors.
 *
 * @author Jesse Cox
 * @version 1.0
 */
//TODO: don't forget to document each method in all classes!
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

public class Assembler {

    // ALGORITHM:
    // get input file name
    // create output file name and stream

    // create symbol table
    // do first pass to build symbol table (no output yet!)
    // do second pass to output translated ASM to HACK code

    // print out "done" message to user
    // close output file stream
    public static void main(String[] args) {

        String inputFileName, outputFileName;
        PrintWriter outputFile = null; //keep compiler happy
        SymbolTable symbolTable;

        //get input file name from command line or console input
        if(args.length == 1) {
            System.out.println("command line arg = " + args[0]);
            inputFileName = args[0];
        }
        else
        {
            Scanner keyboard = new Scanner(System.in);

            System.out.println("Please enter assembly file name you would like to assemble.");
            System.out.println("Don't forget the .asm extension: ");
            //inputFileName = keyboard.nextLine();
            inputFileName = "Mult.asm";
            keyboard.close();
        }

        outputFileName = inputFileName.substring(0,inputFileName.lastIndexOf('.')) + ".hack";

        try {
            outputFile = new PrintWriter(new FileOutputStream(outputFileName));
        } catch (FileNotFoundException ex) {
            System.err.println("Could not open output file " + outputFileName);
            System.err.println("Run program again, make sure you have write permissions, etc.");
            System.exit(0);
        }
        SymbolTable mySymbolTable = new SymbolTable();
        firstPass(inputFileName,mySymbolTable);
        secondPass(inputFileName,mySymbolTable,outputFile);
        outputFile.close();
    }

    /**
     * March through the source code without generating code. Each time an L Command is reached,
     * add it to the symbol table with an appropriate ROM address, which is incremented every
     * time an A Command or C Command is recognized.
     *
     * @param inputFileName
     *          String representing the user defined input file.
     * @param symbolTable
     *          Local symbolTable.
     */
    private static void firstPass(String inputFileName, SymbolTable symbolTable) {
        // ROM Address tracker.
        int romAdd = 0;

        // New custom Parser
        Parser firstParser = new Parser(inputFileName);

        // While the parser still has commands to read:
        //      Advance, parse, and look for L_COMMAND symbols to add to symbolTable.
        //      Otherwise, advance the ROM tracker.
        while (firstParser.hasMoreCommands()) {
            firstParser.advance();
            firstParser.parse();
            if (firstParser.getCommandType() == 'A' || firstParser.getCommandType() == 'C') {
                romAdd++;
            } else if (firstParser.getCommandType() == 'L') {
                //System.out.println("Adding " + firstParser.getSymbol() + " to table");
                symbolTable.addEntry(firstParser.getSymbol(),(romAdd + 1));
            }
        }
    }

    /**
     * Move through the source code again and add lines of binary code to the output file
     * according to the following conditions:
     * - A Command - If the symbol is a proper integer, convert it to a binary string and write.
     *                  else, recognize as variable or label and treat accordingly.
     * - C Command - Use a Code object to lookup the correct binary code for Comp, Dest and Jump,
     *                  and add them to the toWrite String.
     *
     * @param inputFileName
     *          String representing the user defined input file.
     * @param symbolTable
     *          Local symbolTable.
     * @param outputFile
     *          PrintWriter that will write to the .HACK file.
     */
    private static void secondPass(String inputFileName, SymbolTable symbolTable, PrintWriter outputFile) {
        //  Empty RAM tracker
        int emptyPos = 16;

        //  Custom Parser for second pass.
        Parser secondParser = new Parser(inputFileName);

        //  While the parser still has commands to parse:
        //      Create a new String to write to the .HACK file.
        //      Advance and parse.
        //      Depending on the command type, append the appropriate string to the toWrite String
        //      by using a Code object as a lookup table.
        while (secondParser.hasMoreCommands()) {
            secondParser.advance();
            secondParser.parse();
            switch (secondParser.getCommandType()) {
                //  A COMMAND
                case 'A' :
                    String aToWrite = "";
                    aToWrite += "0";
                    try {
                        int symbolInt = Integer.parseInt(secondParser.getSymbol());
                        String symbolString = Integer.toBinaryString(symbolInt);
                        aToWrite += String.format("%15s",symbolString).replaceAll(" ","0");

                    //  If the symbol isn't a proper number, recognize it as a label or variable.
                    } catch (NumberFormatException nfe) {
                        if (!(symbolTable.contains(secondParser.getSymbol()))) {
                            symbolTable.addEntry(secondParser.getSymbol(),emptyPos);
                            emptyPos++;             //  Track new empty RAM position.
                        }
                        int symbolInt = symbolTable.getAddress((secondParser.getSymbol()));
                        String symbolString = Integer.toBinaryString(symbolInt);
                        aToWrite += String.format("%15s",symbolString).replaceAll(" ","0");
                    }
                    aToWrite += "\n";
                    System.out.print(aToWrite);
                    outputFile.print(aToWrite);
                    break;

                //  C COMMAND
                case 'C' :
                    String cToWrite = "";
                    Code cCode = new Code();
                    cToWrite += "111";
                    cToWrite += cCode.getComp(secondParser.getComp());
                    cToWrite += cCode.getDest(secondParser.getDest());
                    cToWrite += cCode.getJump(secondParser.getJump());
                    cToWrite += "\n";
                    System.out.print(cToWrite);
                    outputFile.print(cToWrite);
                    break;

                //  L COMMAND
                case 'L' :
                    symbolTable.addEntry(secondParser.getSymbol(),secondParser.getLineNumber() + 1);
                    break;
                default :
                    break;
            }
        }
    }
}
