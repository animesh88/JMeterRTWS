/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BuildingReducedWSDL;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author ani
 */
public class BuildMessage {

    public static void main(String[] args) throws FileNotFoundException {
        String fileNameRead = "C:\\Users\\ani\\Desktop\\AfterGS1\\Eucalyptus 1 month ago.xml";
        File file = new File(fileNameRead);
        String[] s = {"DescribeSensors", "BundleRestartInstance"};
        BuildMessage obj = new BuildMessage();
        String message = obj.buildMessage(file, s);
    }

    public String buildMessage(File fileNameRead, String s[]) throws FileNotFoundException {

        String message = "";
        Scanner fileToRead = null;
        StringBuffer stringBufferOfData = new StringBuffer();

        fileToRead = new Scanner(fileNameRead); //point the scanner method to a file
        //check if there is a next line and it is not null and then read it in
        for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
            stringBufferOfData.append(line).append("\r\n");
        }

        System.out.println("length " + s.length);
        boolean y = stringBufferOfData.toString().contains("wsdl:message");
        if (y) {
            boolean z = stringBufferOfData.toString().contains("messageResponse");
            if (z) {
                for (int i = 0; i <= s.length && s[i] != null; i++) {
                    String startmessage = "<wsdl:message name=\"" + s[i] + "Response\">";
                    System.out.println(" messageResponse " + s[i]);
                    String endmessage = "</wsdl:message>";
                    //Scanner fileToRead = null;
                    fileToRead = new Scanner(fileNameRead); //point the scanner method to a file
                    //check if there is a next line and it is not null and then read it in
                    int startIndexmessage = stringBufferOfData.indexOf(startmessage);//now we get the starting point of the text we want to edit
                    System.out.println("startIndex " + startIndexmessage);
                    int endIndexmessage = stringBufferOfData.indexOf(endmessage, startIndexmessage);//now we get the starting point of the text we want to edit
                    System.out.println("endIndex" + endIndexmessage);
                    //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
                    CharSequence messageResponse = stringBufferOfData.subSequence(startIndexmessage, endIndexmessage);
                    messageResponse = messageResponse.toString().concat(endmessage + "\n");
                    //System.out.print(messageResponse + "message");
                    message = message.concat(messageResponse.toString());
                }

                for (int i = 0; s[i] != null; i++) {
                    String startmessage = "<wsdl:message name=\"" + s[i] + "\">";
                    String endmessage = "</wsdl:message>";
                    //Scanner fileToRead = null;
                    fileToRead = new Scanner(fileNameRead); //point the scanner method to a file
                    //check if there is a next line and it is not null and then read it in
                    int startIndexmessage = stringBufferOfData.indexOf(startmessage);//now we get the starting point of the text we want to edit
                    //System.out.println(startIndexmessage);
                    int endIndexmessage = stringBufferOfData.indexOf(endmessage, startIndexmessage);//now we get the starting point of the text we want to edit
                    //System.out.println(endIndexmessage);
                    //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
                    CharSequence messagepart = stringBufferOfData.subSequence(startIndexmessage, endIndexmessage);
                    messagepart = messagepart.toString().concat(endmessage + "\n");
                    //System.out.print(messagepart + "message part");
                    message = message.concat(messagepart.toString());
                }
            } else {
                for (int i = 0; s[i] != null; i++) {
                    String startmessage = "<wsdl:message name=\"" + s[i] + "\">";
                    String endmessage = "</wsdl:message>";
                    //Scanner fileToRead = null;
                    fileToRead = new Scanner(fileNameRead); //point the scanner method to a file
                    //check if there is a next line and it is not null and then read it in
                    int startIndexmessage = stringBufferOfData.indexOf(startmessage);//now we get the starting point of the text we want to edit
                    //System.out.println(startIndexmessage);
                    int endIndexmessage = stringBufferOfData.indexOf(endmessage, startIndexmessage);//now we get the starting point of the text we want to edit
                    //System.out.println(endIndexmessage);
                    //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
                    CharSequence messagepart = stringBufferOfData.subSequence(startIndexmessage, endIndexmessage);
                    messagepart = messagepart.toString().concat(endmessage + "\n");
                    //System.out.print(messagepart + "message part");
                    message = message.concat(messagepart.toString());
                }

            }
        } else {
            boolean z = stringBufferOfData.toString().contains("messageResponse");
            if (z) {

                for (int i = 0; i <= s.length && s[i] != null; i++) {
                    String startmessage = "<message name=\"" + s[i] + "Response\">";
                    System.out.println(" messageResponse " + s[i]);
                    String endmessage = "</message>";
                    //Scanner fileToRead = null;
                    fileToRead = new Scanner(fileNameRead); //point the scanner method to a file
                    //check if there is a next line and it is not null and then read it in
                    int startIndexmessage = stringBufferOfData.indexOf(startmessage);//now we get the starting point of the text we want to edit
                    System.out.println("startIndex " + startIndexmessage);
                    int endIndexmessage = stringBufferOfData.indexOf(endmessage, startIndexmessage);//now we get the starting point of the text we want to edit
                    System.out.println("endIndex" + endIndexmessage);
                    //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
                    CharSequence messageResponse = stringBufferOfData.subSequence(startIndexmessage, endIndexmessage);
                    messageResponse = messageResponse.toString().concat(endmessage + "\n");
                    //System.out.print(messageResponse + "message");
                    message = message.concat(messageResponse.toString());
                }

                for (int i = 0; s[i] != null; i++) {
                    String startmessage = "<message name=\"" + s[i] + "\">";
                    String endmessage = "</message>";
                    //Scanner fileToRead = null;
                    fileToRead = new Scanner(fileNameRead); //point the scanner method to a file
                    //check if there is a next line and it is not null and then read it in
                    int startIndexmessage = stringBufferOfData.indexOf(startmessage);//now we get the starting point of the text we want to edit
                    //System.out.println(startIndexmessage);
                    int endIndexmessage = stringBufferOfData.indexOf(endmessage, startIndexmessage);//now we get the starting point of the text we want to edit
                    //System.out.println(endIndexmessage);
                    //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
                    CharSequence messagepart = stringBufferOfData.subSequence(startIndexmessage, endIndexmessage);
                    messagepart = messagepart.toString().concat(endmessage + "\n");
                    //System.out.print(messagepart + "message part");
                    message = message.concat(messagepart.toString());
                }
            } else {
                for (int i = 0; s[i] != null; i++) {
                    String startmessage = "<message name=\"" + s[i] + "\">";
                    String endmessage = "</message>";
                    //Scanner fileToRead = null;
                    fileToRead = new Scanner(fileNameRead); //point the scanner method to a file
                    //check if there is a next line and it is not null and then read it in
                    int startIndexmessage = stringBufferOfData.indexOf(startmessage);//now we get the starting point of the text we want to edit
                    //System.out.println(startIndexmessage);
                    int endIndexmessage = stringBufferOfData.indexOf(endmessage, startIndexmessage);//now we get the starting point of the text we want to edit
                    //System.out.println(endIndexmessage);
                    //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
                    CharSequence messagepart = stringBufferOfData.subSequence(startIndexmessage, endIndexmessage);
                    messagepart = messagepart.toString().concat(endmessage + "\n");
                    //System.out.print(messagepart + "message part");
                    message = message.concat(messagepart.toString());
                }

            }
        }
        /*try {
        String fileName1 = "C:\\Users\\ani\\Desktop\\AfterGS1\\ReduceWSDLEucalyptus.xml";
        //StringBuffer stringBufferOfData = new StringBuffer();
        //System.out.println(message);
        BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileName1));
        bufwriter.write(message);//writes the edited string buffer to the new file
        bufwriter.close();//closes the file
        } catch (Exception e) {//if an exception occurs
        }*/
        return message;
    }
}
