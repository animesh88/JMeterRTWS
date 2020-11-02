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
public class BuildPortType {

    public static void main(String[] args) throws FileNotFoundException {
        String fileNameRead = "C:\\Users\\ani\\Desktop\\After GS1\\Eucalyptus 1 month ago.xml";
        File file = new File(fileNameRead);
        String[] s = {"DescribeSensors", "BundleRestartInstance"};

        BuildPortType obj = new BuildPortType();
        String portType = obj.buildportType(file, s);

        try {
            String fileName1 = "C:\\Users\\ani\\Desktop\\After GS1\\ReduceWSDLEucalyptus.xml";
            //StringBuffer stringBufferOfData = new StringBuffer();
            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileName1));
            bufwriter.write(portType);//writes the edited string buffer to the new file
            bufwriter.close();//closes the file
        } catch (Exception e) {//if an exception occurs
        }


    }

    public String buildportType(File fileNameRead, String[] s) throws FileNotFoundException {
        String portType = "";
        StringBuffer stringBufferOfData = new StringBuffer();
        Scanner fileToRead = null;

        fileToRead = new Scanner(fileNameRead); //point the scanner method to a file
        //check if there is a next line and it is not null and then read it in
        for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
            stringBufferOfData.append(line).append("\r\n");//this small line here is to appened all text read in from the file to a string buffer which will be used to edit the contents of the file
        }
        fileToRead.close();//this is used to release the scanner from file

        //this is used to release the scanner from file
        boolean y = stringBufferOfData.toString().contains("wsdl:portType");
        if (y) {
        String start = "<wsdl:portType ";
        String end = ">";

            int startIndex = stringBufferOfData.indexOf(start);//now we get the starting point of the text we want to edit
            //System.out.println(startIndex);
            int endIndex = stringBufferOfData.indexOf(end, startIndex);//now we get the starting point of the text we want to edit
            //System.out.println(endIndex);
            //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
            CharSequence port = stringBufferOfData.subSequence(startIndex, endIndex);
            port = port.toString().concat(">\n");

            for (int i = 0; i < s.length && s[i] != null; i++) {
                String startOperation = "<wsdl:operation name=\"" + s[i] + "\">";
                String endOperation = "</wsdl:operation>";

                //Scanner fileToRead = null;
                fileToRead = new Scanner(fileNameRead); //point the scanner method to a file
                //check if there is a next line and it is not null and then read it in
                for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
                    stringBufferOfData.append(line).append("\r\n");
                }
                int startIndexOperation = stringBufferOfData.indexOf(startOperation);//now we get the starting point of the text we want to edit
                //System.out.println(startIndexOperation);
                int endIndexOperation = stringBufferOfData.indexOf(endOperation, startIndexOperation);//now we get the starting point of the text we want to edit
                //System.out.println(endIndexOperation);
                //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
                CharSequence str = stringBufferOfData.subSequence(startIndexOperation, endIndexOperation);

                String str1 = str.toString().concat(endOperation + "\n");
                //System.out.println("str1" + str1);
                port = port.toString().concat(str1);

            }
            portType = (port.toString()).concat("</wsdl:portType>");
            //System.out.print(portType + "portType");
        } else {


        String start = "<portType ";
        String end = ">";

           int startIndex = stringBufferOfData.indexOf(start);//now we get the starting point of the text we want to edit
        //System.out.println(startIndex);
        int endIndex = stringBufferOfData.indexOf(end, startIndex);//now we get the starting point of the text we want to edit
        //System.out.println(endIndex);
        //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
        CharSequence port = stringBufferOfData.subSequence(startIndex, endIndex);
        port = port.toString().concat(">\n");

        for (int i = 0; i < s.length && s[i]!=null; i++) {
            String startOperation = "<operation name=\"" + s[i] + "\">";
            String endOperation = "</operation>";

            //Scanner fileToRead = null;
            fileToRead = new Scanner(fileNameRead); //point the scanner method to a file
            //check if there is a next line and it is not null and then read it in
            for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
                stringBufferOfData.append(line).append("\r\n");
            }
            int startIndexOperation = stringBufferOfData.indexOf(startOperation);//now we get the starting point of the text we want to edit
            //System.out.println(startIndexOperation);
            int endIndexOperation = stringBufferOfData.indexOf(endOperation, startIndexOperation);//now we get the starting point of the text we want to edit
            //System.out.println(endIndexOperation);
            //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
            CharSequence str = stringBufferOfData.subSequence(startIndexOperation, endIndexOperation);

            String str1 = str.toString().concat(endOperation + "\n");
            //System.out.println("str1" + str1);
            port = port.toString().concat(str1);
       
        }
        portType = (port.toString()).concat("</portType>");
        //System.out.print(portType + "portType");

        }

        return portType;
    }
}





