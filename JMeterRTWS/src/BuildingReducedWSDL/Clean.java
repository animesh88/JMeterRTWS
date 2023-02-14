/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BuildingReducedWSDL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**s
 *
 * @author ani
 */
public class Clean {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String patchFileName = "C:\\Users\\ani\\Desktop\\AfterGS1\\EucalyptusPatch.patch";
        Clean obj = new Clean();
        obj.cleanPatch(patchFileName);
    }

    void cleanPatch(String patchFileName) throws IOException {
        
        FileReader reader = null;
        try {
            reader = new FileReader(new File(patchFileName));
        } catch (FileNotFoundException ex) {
        }
        BufferedReader bufferReader = new BufferedReader(reader);
        String str = null;
        int startIndex =0, endIndex =0;

        while ((bufferReader.readLine()) != null && startIndex != -1 && endIndex != -1) {

            try {
                String start = "> ";
                String end = "<";
                String replacementText = "";
                Scanner fileToRead = null;
                StringBuffer stringBufferOfData = new StringBuffer();

                try {
                    fileToRead = new Scanner(new File(patchFileName)); //point the scanner method to a file
                    //check if there is a next line and it is not null and then read it in
                    for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
                        stringBufferOfData.append(line).append("\r\n");//this small line here is to appened all text read in from the file to a string buffer which will be used to edit the contents of the file
                    }
                    fileToRead.close();//this is used to release the scanner from file
                } catch (FileNotFoundException ex) {
                }

                startIndex = stringBufferOfData.indexOf(start);//now we get the starting point of the text we want to edit
                System.out.println(startIndex);
                endIndex = stringBufferOfData.indexOf(end, startIndex);//now we get the starting point of the text we want to edit
                System.out.println(endIndex);

                //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
                stringBufferOfData.replace(startIndex, endIndex, replacementText);

                try {
                    BufferedWriter bufwriter = new BufferedWriter(new FileWriter(patchFileName));
                    bufwriter.write(stringBufferOfData.toString());//writes the edited string buffer to the new file
                    bufwriter.close();//closes the file
                } catch (Exception e) {//if an exception occurs
                }

            } catch (Exception e) {
            }
        }

    }
}
