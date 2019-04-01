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
            inputFileName = keyboard.nextLine();

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
    }

    // TODO: march through the source code without generating any code
    //for each label declaration (LABEL) that appears in the source code,
    // add the pair <LABEL, n> to the symbol table
    // n = romAddress which you should keep track of as you go through each line
    //HINT: when should rom address increase? For what kind of commands?
    private static void firstPass(String inputFileName, SymbolTable symbolTable) {
        int romAdd = 0;
        Parser firstParser = new Parser(inputFileName);
        while (firstParser.hasMoreCommands()) {
            firstParser.advance();
            firstParser.parseCommandType();
            if (firstParser.getCommandType() == 'A' || firstParser.getCommandType() == 'C') {
                romAdd++;
            } else if (firstParser.getCommandType() == 'L') {
                symbolTable.addEntry(firstParser.getSymbol(),(romAdd + 1));
            }
        }
    }

    // TODO: march again through the source code and process each line:
    // if the line is a c-instruction, simple (translate)
    // if the line is @xxx where xxx is a number, simple (translate)
    // if the line is @xxx and xxx is a symbol, look it up in the symbol
    //	table and proceed as follows:
    // If the symbol is found, replace it with its numeric value and
    //	and complete the commands translation
    // If the symbol is not found, then it must represent a new variable:
    // add the pair <xxx, n> to the symbol table, where n is the next
    //	available RAM address, and complete the commands translation
    // HINT: What should ram address start at? When should it increase?
    // What do you do with L commands and No commands?
    private static void secondPass(String inputFileName, SymbolTable symbolTable, PrintWriter outputFile) {
        int emptyPos = 16;
        Parser secondParser = new Parser(inputFileName);
        while (secondParser.hasMoreCommands()) {
            String toWrite = "";
            secondParser.advance();
            secondParser.parseCommandType();
            switch (secondParser.getCommandType()) {
                case 'A' :
                    toWrite += "1";
                    try {
                        toWrite.concat("" + Integer.toBinaryString(Integer.parseInt(secondParser.getSymbol())));
                    } catch (NumberFormatException nfe) {
                        if (!(symbolTable.contains(secondParser.getSymbol()))) {
                            symbolTable.addEntry(secondParser.getSymbol(),emptyPos);
                            emptyPos++;
                        }
                        toWrite.concat("" + Integer.toBinaryString(symbolTable.getAddress(secondParser.getSymbol())));
                    }
                    break;
                case 'C' :
                    Code cCode = new Code();
                    toWrite += "0";
                    toWrite.concat(cCode.getComp(secondParser.getComp()));
                    toWrite.concat(cCode.getDest(secondParser.getDest()));
                    toWrite.concat(cCode.getJump(secondParser.getJump()));
                    break;
                case 'L' :
                    break;
                default :
                    break;
            }
            outputFile.print(toWrite);
        }
    }



}
