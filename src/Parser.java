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
        this.inputFile = new Scanner(inFileName);
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
        this.reset();
        this.lineNumber++;
        this.rawLine = this.inputFile.nextLine();
        this.cleanLine();
    }


    // PARSING HELPERS

    /**
     * Removes comments and whitespace.
     */
    public void cleanLine() {
        int comments = this.rawLine.indexOf("//");
        this.cleanLine = this.rawLine.substring(0,comments);
        this.cleanLine = this.rawLine.trim();
    }

    /**
     * Sets the command type according to the first character of the clean line.
     */
    public void parseCommandType() {
        switch(this.cleanLine.charAt(0)) {
            case '@' :
                this.commandType = A_COMMAND;
                break;
            case '(' :
                this.commandType = L_COMMAND;
                break;
            default :
                this.commandType = C_COMMAND;
                break;
        }
    }

    public void parse() {
        this.parseCommandType();
        if (this.commandType == A_COMMAND || this.commandType == L_COMMAND) {
            this.parseSymbol();
        }
        if (this.commandType == C_COMMAND) {
            this.parseDest();
            this.parseComp();
            this.parseJump();
        }
    }

    /**
     * Parses symbol for A- or L- commands. Calls advance() so cleanLine has a value.
     */
    public void parseSymbol() {
        this.advance();
        if (this.getCommandType() == 'A') {
            int aSymbol = Integer.parseInt(this.cleanLine.substring(1),2);
            this.symbol = ("" + aSymbol);
        } else if (this.getCommandType() == 'L') {
            this.symbol = this.cleanLine.substring(1,this.cleanLine.length()-1);
        }
    }

    /**
     * Parses line to get dest. Will only be called if a C instruction.
     */
    public void parseDest() {
        if (this.cleanLine.contains("=")) {
            int equalsSign = this.cleanLine.indexOf('=');
            this.destMnemonic = this.cleanLine.substring(0,equalsSign);
        }
    }

    /**
     * Parses line to get comp. Will only be called if a C instruction.
     */
    public void parseComp() {
        if (this.cleanLine.contains("=")) {
            int equalsSign = this.cleanLine.indexOf('=');
            this.compMnemonic = this.cleanLine.substring(equalsSign,this.cleanLine.length());
        } else if (this.cleanLine.contains(";")) {
            int semicolon = this.cleanLine.indexOf(';');
            this.compMnemonic = this.cleanLine.substring(0,semicolon);
        }
    }

    /**
     * Parses line to get jump. Will only be called if a C instruction.
     */
    public void parseJump() {
        if (this.cleanLine.contains(";")) {
            int semicolon = this.cleanLine.indexOf(';');
            this.jumpMnemonic = this.cleanLine.substring(semicolon);
        }
    }

    /**
     * Resets all instance variables.
     */
    public void reset() {
        this.rawLine = "";
        this.cleanLine = "";
        this.commandType = '\u0000';
        this.symbol = "";
        this.destMnemonic = "";
        this.compMnemonic = "";
        this.jumpMnemonic = "";
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
