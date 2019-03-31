import java.util.HashMap;

/**
 * SymbolTable.java : Manages the symbol table. Keeps a correspondence between
 *                    symbolic labels and numeric addresses.
 *
 * @author Jesse Cox
 * @version 1.0
 */

public class SymbolTable {

    // DEFAULT VALUES
    private static final String INITIAL_VALID_CHARS = "";
    private static final String ALL_VALID_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_";

    // SINGLE INSTANCE VARIABLE
    private HashMap<String, Integer> symbolTable;

    // SIMPLE CONSTRUCTOR
    /**
     * Initializes hashmap with predefined symbols.
     */
    public SymbolTable() {
        symbolTable = new HashMap<>();

        // SYMBOLTABLE INITIALIZATION
        symbolTable.put("R0",0);        symbolTable.put("R1",1);
        symbolTable.put("R2",2);        symbolTable.put("R3",3);
        symbolTable.put("R4",4);        symbolTable.put("R5",5);
        symbolTable.put("R6",6);        symbolTable.put("R7",7);
        symbolTable.put("R8",8);        symbolTable.put("R9",9);
        symbolTable.put("R10",10);      symbolTable.put("R11",11);
        symbolTable.put("R12",12);      symbolTable.put("R13",13);
        symbolTable.put("R14",14);      symbolTable.put("R15",15);

        symbolTable.put("SCREEN",16384);
        symbolTable.put("KBD",24576);   symbolTable.put("SP",0);
        symbolTable.put("LCL",1);       symbolTable.put("ARG",2);
        symbolTable.put("THIS",3);      symbolTable.put("THAT",4);
        symbolTable.put("WRITE",18);    symbolTable.put("END",22);
        symbolTable.put("i",16);        symbolTable.put("sum",17);
    }

    /**
     * Adds new pair of symbol / address to the symbolTable HashMap.
     *
     * @param symbol
     *          String representing a new symbol to add to the Hashmap.
     * @param address
     *          integer value for the address of the new symbol to be added.
     * @return true if able to add, false if illegal name.
     */
    public boolean addEntry(String symbol, int address) {
        if (!(this.contains(symbol))) {
            symbolTable.put(symbol,address);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns boolean of whether the symbolTable contains a given symbol.
     *
     * @param symbol
     *          String representing the symbol to be checked.
     * @return True if this symbolTable contains the given symbol, otherwise false.
     */
    public boolean contains(String symbol) {
        return symbolTable.containsKey(symbol);
    }

    /**
     * Returns address in hashmap of given symbol
     *
     * @param symbol
     *          String representing the symbol to be found.
     * @return address of the given symbol.
     */
    public int getAddress(String symbol) {
        return symbolTable.get(symbol);
    }

    /**
     * Returns boolean of whether the symbol is a valid symbol name or not.
     *
     * @param symbol
     *          String representing the symbol to be checked if valid or not.
     * @return True if the symbol is valid, false otherwise.
     */
    public boolean isValidName(String symbol) {
        boolean toReturn = true;
        char[] charray = symbol.toCharArray();
        for (char c : charray) {
            if (!(ALL_VALID_CHARS.contains(""+c))) {
                toReturn = false;
            }
        }
        return toReturn;
    }
}
