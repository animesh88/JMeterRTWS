/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BuildingDifferenceWSDL;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

/**
 *
 * @author ani
 */
public class EndServiceDefinition {

    public static void main(String[] args) throws FileNotFoundException {

        String fileName = "C:\\Users\\ani\\Desktop\\After GS1\\Eucalyptus 1 month ago.xml";
        File file = new File(fileName);
        EndServiceDefinition obj = new EndServiceDefinition();
        CharSequence x = obj.buildDefinition(file);
        try {
            String fileName1 = "C:\\Users\\ani\\Desktop\\GS1\\ReduceWSDL.xml";
            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileName1));
            bufwriter.write(x.toString());//writes the edited string buffer to the new file
            bufwriter.close();//closes the file
        } catch (Exception e) {//if an exception occurs
        }

    }

    String buildDefinition(File fileName) throws FileNotFoundException {

        String out = null;
        StringBuffer stringBufferOfData = new StringBuffer();
        Scanner fileToRead = null;
        fileToRead = new Scanner(fileName); //point the scanner method to a file
        //check if there is a next line and it is not null and then read it in
        for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
            stringBufferOfData.append(line).append("\r\n");
        }
        boolean y = stringBufferOfData.toString().contains("wsdl:service");
        if (y) {
            String start = "<wsdl:service";
            String end = "</wsdl:definitions>";
            
            int startIndex = stringBufferOfData.indexOf(start);//now we get the starting point of the text we want to edit
            System.out.println(startIndex);
            int endIndex = stringBufferOfData.indexOf(end);//now we get the starting point of the text we want to edit
            System.out.println(endIndex);
            //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
            CharSequence x = stringBufferOfData.subSequence(startIndex, endIndex);
            x = x.toString().concat(end);
            out = x.toString();
        } else {
            System.out.println(stringBufferOfData.toString());
            boolean z = stringBufferOfData.toString().contains("</wsdl:definitions>");
            if (z) {
                String start = "<service";
                String end = "</wsdl:definitions>";
                
                int startIndex = stringBufferOfData.indexOf(start);//now we get the starting point of the text we want to edit
                System.out.println(startIndex);
                int endIndex = stringBufferOfData.indexOf(end);//now we get the starting point of the text we want to edit
                System.out.println(endIndex);
                //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
                CharSequence x = stringBufferOfData.subSequence(startIndex, endIndex);
                x = x.toString().concat(end);
                out = x.toString();
            } else {
                String start = "<service";
                String end = "</definitions>";
                
                int startIndex = stringBufferOfData.indexOf(start);//now we get the starting point of the text we want to edit
                System.out.println(startIndex);
                int endIndex = stringBufferOfData.indexOf(end);//now we get the starting point of the text we want to edit
                System.out.println(endIndex);
                //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
                CharSequence x = stringBufferOfData.subSequence(startIndex, endIndex);
                x = x.toString().concat(end);
                out = x.toString();

            }
        }

        return out;
    }
}
