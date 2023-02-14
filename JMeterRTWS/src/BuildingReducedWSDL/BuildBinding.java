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
public class BuildBinding {

    public static void main(String[] args) throws FileNotFoundException {
        String fileNameRead = "C:\\Users\\ani\\Desktop\\After GS1\\Eucalyptus 1 month ago.xml";
        File file = new File(fileNameRead);
        String[] s = {"DescribeSensors", "BundleRestartInstance"};
        BuildBinding obj = new BuildBinding();
        String binding = obj.buildBinding(file, s);

        try {
            String fileName1 = "C:\\Users\\ani\\Desktop\\After GS1\\ReduceWSDLEucalyptus.xml";
            //StringBuffer stringBufferOfData = new StringBuffer();

            System.out.println(binding);
            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileName1));
            bufwriter.write(binding);//writes the edited string buffer to the new file
            bufwriter.close();//closes the file
        } catch (Exception e) {//if an exception occurs
        }

    }

    public String buildBinding(File fileNameRead, String[] s) throws FileNotFoundException {

        String operation = "";
        Scanner fileToRead = null;
        StringBuffer stringBufferOfData = new StringBuffer();
        fileToRead = new Scanner(fileNameRead); //point the scanner method to a file
            //check if there is a next line and it is not null and then read it in
            for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
                stringBufferOfData.append(line).append("\r\n");
            }
        boolean y = stringBufferOfData.toString().contains("wsdl:binding");
        if (y) {
            String start = "<wsdl:binding ";
            String end = "\"/>";
            //check if there is a next line and it is not null and then read it in
            
            int startIndex = stringBufferOfData.indexOf(start);//now we get the starting point of the text we want to edit
            //System.out.println(startIndex);
            int endIndex = stringBufferOfData.indexOf(end, startIndex);//now we get the starting point of the text we want to edit
            //System.out.println(endIndex);
            //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
            CharSequence port = stringBufferOfData.subSequence(startIndex, endIndex);
            port = port.toString().concat("\"/>\n");
            operation = operation.toString().concat(port.toString());

            for (int i = 0; i < s.length && s[i] != null; i++) {
                String startoperation = "<wsdl:operation name=\"" + s[i] + "\">";
                String endoperation = "</wsdl:operation>";
                //Scanner fileToRead = null;
                //check if there is a next line and it is not null and then read it in
                int startIndexoperation = stringBufferOfData.indexOf(startoperation, startIndex);//now we get the starting point of the text we want to edit
                //System.out.println(startIndexoperation);
                int endIndexoperation = stringBufferOfData.indexOf(endoperation, startIndexoperation);//now we get the starting point of the text we want to edit
                //System.out.println(endIndexoperation);
                //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
                CharSequence str = stringBufferOfData.subSequence(startIndexoperation, endIndexoperation);

                String str1 = str.toString().concat(endoperation + "\n");
                // System.out.println("str1" + str1);
                operation = operation.concat(str1);

            }
            //System.out.print(operation + "operation");
            operation = operation.concat("</wsdl:binding>");
        }
        
        else {
            String start = "<binding ";
            String end = "\"/>";
            //check if there is a next line and it is not null and then read it in
            for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
                stringBufferOfData.append(line).append("\r\n");
            }
            int startIndex = stringBufferOfData.indexOf(start);//now we get the starting point of the text we want to edit
            //System.out.println(startIndex);
            int endIndex = stringBufferOfData.indexOf(end, startIndex);//now we get the starting point of the text we want to edit
            //System.out.println(endIndex);
            //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
            CharSequence port = stringBufferOfData.subSequence(startIndex, endIndex);
            port = port.toString().concat("\"/>\n");
            operation = operation.toString().concat(port.toString());

            for (int i = 0; i < s.length && s[i] != null; i++) {
                String startoperation = "<operation name=\"" + s[i] + "\">";
                String endoperation = "</operation>";
                //Scanner fileToRead = null;
                fileToRead = new Scanner(fileNameRead); //point the scanner method to a file
                //check if there is a next line and it is not null and then read it in
                int startIndexoperation = stringBufferOfData.indexOf(startoperation, startIndex);//now we get the starting point of the text we want to edit
                //System.out.println(startIndexoperation);
                int endIndexoperation = stringBufferOfData.indexOf(endoperation, startIndexoperation);//now we get the starting point of the text we want to edit
                //System.out.println(endIndexoperation);
                //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
                CharSequence str = stringBufferOfData.subSequence(startIndexoperation, endIndexoperation);

                String str1 = str.toString().concat(endoperation + "\n");
                // System.out.println("str1" + str1);
                operation = operation.concat(str1);

            }
            //System.out.print(operation + "operation");
            operation = operation.concat("</binding>");
        }
 return operation;
    }
}
