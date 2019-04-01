import java.util.HashMap;

/**
 * Code.java : Translates Hack assembly language mnemonics into binary codes.
 *
 * @author Jesse Cox
 * @version 1.0
 */



public class Code {
    // INSTANCE VARIABLES
    private HashMap<String, String> compCodes;
    private HashMap<String, String> destCodes;
    private HashMap<String, String> jumpCodes;

    // SIMPLE CONSTRUCTOR
    /**
     * Initializes hashmaps with binary codes for easy lookup.
     */
    public Code() {
        compCodes = new HashMap<>(28);
        destCodes = new HashMap<>(8);
        jumpCodes = new HashMap<>(8);

        // COMP INITIALIZATION
        compCodes.put("0","0101010");       compCodes.put("1","0111111");
        compCodes.put("-1","0111010");      compCodes.put("D","0001100");
        compCodes.put("A","0110000");       compCodes.put("!D","0001101");
        compCodes.put("!A","0110001");      compCodes.put("-D","0001111");
        compCodes.put("-A","0110011");      compCodes.put("D+1","0011111");
        compCodes.put("A+1","0110111");     compCodes.put("D-1","0001110");
        compCodes.put("A-1","0110010");     compCodes.put("D+A","0000010");
        compCodes.put("D-A","0010011");     compCodes.put("A-D","0000111");
        compCodes.put("D&A","0000000");     compCodes.put("D|A","0010101");
        compCodes.put("M","1110000");       compCodes.put("!M","1110001");
        compCodes.put("-M","1110011");      compCodes.put("M+1","1110111");
        compCodes.put("M-1","1110010");     compCodes.put("D+M","1000010");
        compCodes.put("D-M","1010011");     compCodes.put("M-D","1000111");
        compCodes.put("D&M","1000000");     compCodes.put("D|M","1010101");

        // DEST INITIALIZATION
        destCodes.put("null","000");        destCodes.put("M","001");
        destCodes.put("D","010");           destCodes.put("MD","011");
        destCodes.put("A","100");           destCodes.put("AM","101");
        destCodes.put("AD","110");          destCodes.put("AMD","111");

        // JUMP INITIALIZATION
        jumpCodes.put("null","000");        jumpCodes.put("JGT","001");
        jumpCodes.put("JEQ","010");         jumpCodes.put("JGE","011");
        jumpCodes.put("JLT","100");         jumpCodes.put("JNE","101");
        jumpCodes.put("JLE","101");         jumpCodes.put("JMP","111");
    }

    // ACCESSOR METHODS

    /**
     * Converts a mnemonic string to a string of 7 bits.
     *
     * @param mnemonic
     *          String that represents the mnemonic for a comp code.
     * @return String held by compCodes HashTable for the given mnemonic String key.
     */
    public String getComp(String mnemonic) {
        return compCodes.get(mnemonic);
    }

    /**
     * Converts a mnemonic String to a String of 3 bits.
     *
     * @param mnemonic
     *          String that represents the mnemonic for a dest code.
     * @return String held by destCodes HashTable for the given mnemonic String key.
     */
    public String getDest(String mnemonic) {
        return destCodes.get(mnemonic);
    }

    /**
     * Converts a mnemonic String to a String of 3 bits.
     *
     * @param mnemonic
     *          String that represents the mnemonic for a jump code.
     * @return String held by jumpCodes HashTable for the given mnemonic String key.
     */
    public String getJump(String mnemonic) {
        return jumpCodes.get(mnemonic);
    }
}
