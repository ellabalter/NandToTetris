import java.util.HashMap;

public class symbolTable {
    private HashMap<String,Integer> symbolTable;

    public symbolTable(){
       symbolTable = new HashMap<>();
   }

    public void setSymbolTable(HashMap<String, Integer> symbolTable) {
        this.symbolTable = symbolTable;
    }
    public void addEntry(String symbol, int address) {
        symbolTable.put(symbol, address);
    }
    public boolean contains(String symbol) {
        return symbolTable.containsKey(symbol);
    }
    public int getAddress(String symbol) {
        if (symbolTable.containsKey(symbol)) {
            return symbolTable.get(symbol);
        } else {
            throw new IllegalArgumentException("Symbol not found: " + symbol);
        }
    }




}
