import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Parser.java : Unpacks each command into its underlying fields.
 *
 * @author Jesse Cox
 * @version 1.0
 */


public class Parser {

    // DEFAULT VALUES
    private static final char NO_COMMAND = 'N';
    private static final char A_COMMAND = 'A';
    private static final char C_COMMAND = 'C';
    private static final char L_COMMAND = 'L';

    // FILE STUFF + DEBUGGING
    private Scanner inputFile;
    private int lineNumber;
    private String rawLine;

    // PARSED COMMAND PARTS
    private String cleanLine;
    private char commandType;
    private String symbol;
    private String destMnemonic;
    private String compMnemonic;
    private String jumpMnemonic;


    // DRIVERS

    /**
     * Constructor opens input file / stream and prepares to parse. If file can't be
     * opened, ends program with error message.
     *
     * @param inFileName file name exists and is a proper ASM file.
     */
    public Parser(String inFileName) {
        this.lineNumber = 0;
        File inFile = new File(inFileName);
        try {
            this.inputFile = new Scanner(inFile);
        } catch (FileNotFoundException fnfe) {
            System.out.println("Oops! File not found!");
            System.exit(0);
        }
    }

    /**
     * Returns true if more commands exist, otherwise closes stream and returns false.
     *
     * @return true if there are more commands, otherwise false.
     */
    public boolean hasMoreCommands() {
        return this.inputFile.hasNext();
    }

    /**
     * Reads next line from file and parses it into instance variables.
     */
    public void advance() {
        reset();
        lineNumber++;
        rawLine = inputFile.nextLine();
        cleanLine();
    }


    // PARSING HELPERS

    /**
     * Removes comments and whitespace.
     */
    public void cleanLine() {
        cleanLine = rawLine;
        if (rawLine.contains("//")) {
            int comments = rawLine.indexOf("//");
            cleanLine = cleanLine.substring(0,comments);
        }
        cleanLine = cleanLine.replaceAll("\\s","");
        cleanLine = cleanLine.trim();
    }

    /**
     * Sets the command type according to the first character of the clean line.
     */
    public void parseCommandType() {
        if (!(cleanLine.isEmpty())) {
        }
        if (cleanLine.isEmpty()) {
            commandType = NO_COMMAND;
        } else if (cleanLine.charAt(0) == '@') {
            commandType = A_COMMAND;
        } else if (cleanLine.charAt(0) == '(') {
            commandType = L_COMMAND;
        } else {
            commandType = C_COMMAND;
        }
    }

    public void parse() {
        parseCommandType();
        if (commandType == A_COMMAND || commandType == L_COMMAND) {
            parseSymbol();
        }
        if (commandType == C_COMMAND) {
            parseDest();
            parseComp();
            parseJump();
        }
    }

    /**
     * Parses symbol for A- or L- commands. Calls advance() so cleanLine has a value.
     */
    public void parseSymbol() {
        if (getCommandType() == 'A') {
            symbol = cleanLine.substring(1);
        } else if (getCommandType() == 'L') {
            symbol = cleanLine.substring(1,cleanLine.length()-1);
        }
    }

    /**
     * Parses line to get dest. Will only be called if a C instruction.
     */
    public void parseDest() {
        if (cleanLine.contains("=")) {
            int equalsSign = cleanLine.indexOf('=');
            destMnemonic = cleanLine.substring(0,equalsSign);
        } else {
            destMnemonic = "null";
        }
    }

    /**
     * Parses line to get comp. Will only be called if a C instruction.
     */
    public void parseComp() {
        if (cleanLine.contains("=")) {
            int equalsSign = cleanLine.indexOf('=');
            compMnemonic = cleanLine.substring(equalsSign + 1);
        } else if (cleanLine.contains(";")) {
            int semicolon = cleanLine.indexOf(';');
            compMnemonic = cleanLine.substring(0,semicolon);
        }
    }

    /**
     * Parses line to get jump. Will only be called if a C instruction.
     */
    public void parseJump() {
        if (cleanLine.contains(";")) {
            int semicolon = cleanLine.indexOf(';');
            jumpMnemonic = cleanLine.substring(semicolon + 1);
        } else {
            jumpMnemonic = "null";
        }
    }

    /**
     * Resets all instance variables.
     */
    public void reset() {
        rawLine = "";
        cleanLine = "";
        commandType = '\u0000';
        symbol = "";
        destMnemonic = "";
        compMnemonic = "";
        jumpMnemonic = "";
    }


    // USEFUL GETTERS

    /**
     * Returns this parser's current command type.
     *
     * @return this.commandType
     */
    public char getCommandType() { return this.commandType; }

    /**
     * Returns this parser's current symbol.
     *
     * @return this.symbol.
     */
    public String getSymbol() { return this.symbol; }

    /**
     * Returns this parser's current dest mnemonic.
     *
     * @return this.destMnemonic.
     */
    public String getDest() { return this.destMnemonic; }

    /**
     * Returns this parser's current comp mnemonic.
     *
     * @return this.compMnemonic.
     */
    public String getComp() { return this.compMnemonic; }

    /**
     * Returns this parser's current jump mnemonic.
     *
     * @return this.jumpMnemonic.
     */
    public String getJump() { return this.jumpMnemonic; }


    // DEBUGGING GETTERS

    /**
     * Returns the original raw instruction.
     *
     * @return this.rawLine
     */
    public String getRawLine() { return this.rawLine; }

    /**
     * Returns this parser's cleaned instruction.
     *
     * @return this.cleanLine
     */
    public String getCleanLine() { return this.cleanLine; }

    /**
     * Returns this parser's current line number in the instruction list.
     *
     * @return this.lineNumber
     */
    public int getLineNumber() { return this.lineNumber; }
}
