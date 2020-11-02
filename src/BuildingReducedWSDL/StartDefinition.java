/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BuildingReducedWSDL;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

/**
 *
 * @author ani
 */
public class StartDefinition {

    public static void main(String[] args) throws FileNotFoundException {
        String fileName = "C:\\Users\\ani\\Desktop\\After GS1\\Eucalyptus 1 month ago.xml";
        File file = new File(fileName);
        StartDefinition obj = new StartDefinition();
        String x = obj.buildDefinition(file);
        try {
            String fileName1 = "C:\\Users\\ani\\Desktop\\After GS1\\ReduceWSDLEucalyptus.xml";
            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileName1));
            bufwriter.write(x);//writes the edited string buffer to the new file
            bufwriter.close();//closes the file
        } catch (Exception e) {//if an exception occurs
        }
    }

    String buildDefinition(File fileName) throws FileNotFoundException {
        Scanner fileToRead = null;
        StringBuffer stringBufferOfData = new StringBuffer();
        fileToRead = new Scanner(fileName); //point the scanner method to a file
        //check if there is a next line and it is not null and then read it in
        for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
            stringBufferOfData.append(line).append("\r\n");
        }
        //String start = "<?xml ";
        boolean y = stringBufferOfData.toString().contains("wsdl:message");
        String out = null;
        if (y) {
            String end = "<wsdl:message";
            fileToRead = new Scanner(fileName); //point the scanner method to a file
            //check if there is a next line and it is not null and then read it in
            for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
                stringBufferOfData.append(line).append("\r\n");
            }
            int startIndex = 0;//stringBufferOfData.indexOf(start);//now we get the starting point of the text we want to edit
            //System.out.println(startIndex);
            int endIndex = stringBufferOfData.indexOf(end);//now we get the starting point of the text we want to edit
            //System.out.println(endIndex);
            //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
            CharSequence x = stringBufferOfData.subSequence(startIndex, endIndex);
            out = x.toString();
        } else {
            String end = "<message";
            fileToRead = new Scanner(fileName); //point the scanner method to a file
            //check if there is a next line and it is not null and then read it in
            for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
                stringBufferOfData.append(line).append("\r\n");
            }
            int startIndex = 0; //stringBufferOfData.indexOf(start);//now we get the starting point of the text we want to edit
            //System.out.println(startIndex);
            int endIndex = stringBufferOfData.indexOf(end);//now we get the starting point of the text we want to edit
            //System.out.println(endIndex);
            //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
            CharSequence x = stringBufferOfData.subSequence(startIndex, endIndex);
            out = x.toString();
        }
        //System.out.println(out);
        return out;
    }
}
