import java.io.*;

public class HackAssembler {

    public void assemble(String inputFilePath, String outputFilePath) throws IOException {
        //initialization
        symbolTable st= new symbolTable();
        code code = new code();
        Parser parser = new Parser(inputFilePath);
        FileWriter Writer = new FileWriter(outputFilePath);
        BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));

        st.addEntry("R0", 0);st.addEntry("R1", 1);
        st.addEntry("R2", 2);st.addEntry("R3", 3);
        st.addEntry("R4", 4);st.addEntry("R5", 5);
        st.addEntry("R6", 6);st.addEntry("R7", 7);
        st.addEntry("R8", 8);st.addEntry("R9", 9);
        st.addEntry("R10", 10);st.addEntry("R11", 11);
        st.addEntry("R12", 12);st.addEntry("R13", 13);
        st.addEntry("R14", 14);st.addEntry("R15", 15);
        st.addEntry("SCREEN", 16384);st.addEntry("KBD",24576);
        st.addEntry("SP", 0);st.addEntry("LCL", 1);
        st.addEntry("ARG", 2);st.addEntry("THIS", 3);
        st.addEntry("THAT",4);

        //firstPass
        int romAddress=0;
        while(parser.hasMoreLines() && parser.advance() != null){
                if (parser.instructionType() == InstructionType.Type.L_INSTRUCTION) {
                    String curLable = parser.symbol();
                    if (!st.contains(curLable)) {
                        st.addEntry(curLable, romAddress);
                    }
                } else {
                    romAddress++;

                }

        }
        parser = new Parser(inputFilePath);
        //main loop

        int ramAddress = 16;
            while( parser.hasMoreLines() && parser.advance() != null){

                if(parser.instructionType() == InstructionType.Type.A_INSTRUCTION){
                    String curLable = parser.symbol();
                    int address;

                    if(!st.contains(curLable)){
                        char firstCharacter = curLable.charAt(0);
                        if(Character.isDigit(firstCharacter)) {
                            address= Integer.parseInt(curLable);
                        }
                        else {
                            st.addEntry(curLable, ramAddress);
                            address = ramAddress;
                            ramAddress++;
                        }
                    }
                    else {
                        address = st.getAddress(curLable);
                    }

                  String  instructions= String.format("%016d", Long.parseLong(Integer.toBinaryString(address))) + "\n";
                    System.out.println("A-instruction: " + instructions);

                    Writer.write(instructions);

                }
                else if (parser.instructionType() == InstructionType.Type.C_INSTRUCTION) {
                    String dest = code.dest(parser.dest());
                    String comp = code.comp(parser.comp());
                    String jump = code.jump(parser.jump());
                    String binaryInstruction = "111"+ comp  + dest + jump;
                    Writer.write(binaryInstruction + "\n");
                }
            }
            Writer.flush();
            Writer.close();

    }

}