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
import com.predic8.wsdl.*;

/**
 *
 * @author ani
 */
public class Search {

    public static void main(String[] args) throws IOException {
        WSDLParser parser = new WSDLParser();

        Definitions defs = parser.parse("C:\\Users\\ani\\Desktop\\AfterGS1\\Eucalyptus 7month ago.xml");

        for (PortType pt : defs.getPortTypes()) {
            System.out.println(pt.getName());
            for (Operation op : pt.getOperations()) {
                System.out.println(" -" + op.getName());
            }
        }
        Search s = new Search();
        String[] out = s.search("C:\\Users\\ani\\Desktop\\AfterGS1\\Eucalyptus 7month ago.xml");

        String[] output;
        output = new String[100];
        int j = 0;

        for (; out.length >= j && out[j] != null; j++) {
            output[j] = out[j];
            System.out.println(out[j]);
        }

    }


    /*

    String fileName = "C:\\Users\\ani\\Desktop\\AfterGS1\\EucalyptusPatch.patch";
    String out[];
    out = new String[100];
    Search obj = new Search();
    out = obj.search(fileName);
    for (int j = 0; out[j] != null ; j++) {
    //String startmessage = "<wsdl:message name=\"" + s[i] + "Response\">";
    System.out.print("\n mainSearch" + out[j]);
    }
    }
     */
    public String[] search(String fileName) throws IOException {
        FileReader reader = null;

        try {
            WSDLParser parser = new WSDLParser();
            Definitions defs = parser.parse(fileName);
            String[] out;
            out = new String[100];
            int i = 0;

            for (PortType pt : defs.getPortTypes()) {
                System.out.println(pt.getName());
                for (Operation op : pt.getOperations()) {
                    System.out.println(i + " -" + op.getName());
                    out[i] = op.getName();
                    i++;
                }
            }
            return out;
        } catch (Exception e) {

            String[] out;
            out = new String[100];
            int i = 0;
            try {
//String dir = "File";
                reader = new FileReader(new File(fileName));
            } catch (FileNotFoundException ex) {
            }
            BufferedReader bufferReader = new BufferedReader(reader);

            Scanner fileToRead = null;
            StringBuffer stringBufferOfData = new StringBuffer();

            int Index = 0, startIndex = 0, endIndex = 0, bindingIndex = 1;
            try {
                fileToRead = new Scanner(new File(fileName)); //point the scanner method to a file
//check if there is a next line and it is not null and then read it in
                for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
                    stringBufferOfData.append(line).append("\r\n");//this small line here is to appened all text read in from the file to a string buffer which will be used to edit the contents of the file
                }
                fileToRead.close();//this is used to release the scanner from file
            } catch (FileNotFoundException except) {
            }

            String start = "<operation name=\"";
            String end = "\">";
            startIndex = stringBufferOfData.indexOf(start, endIndex);
            String binding = "<soap:";
            bindingIndex = stringBufferOfData.indexOf(binding);
            System.out.println(bindingIndex);
            String operation = "";
            while ((bufferReader.readLine()) != null && endIndex != -1 && startIndex != -1 && (bindingIndex > startIndex)) {
                try {
//now we get the starting point of the text we want to edit
                    Index = startIndex + start.length();
//System.out.println(startIndex);
//System.out.println(Index);
                    endIndex = stringBufferOfData.indexOf(end, startIndex);//now we add the staring index of the text with text length to get the end index
//System.out.println(endIndex);
//stringBufferOfData.replace(startIndex, endIndex, replacementText);
                    CharSequence s = stringBufferOfData.subSequence(Index, endIndex);
                    startIndex = stringBufferOfData.indexOf(start, endIndex);//now we get the starting point of the text we want to edit

//StringBuffer append = stringBufferOfData.append(stringBufferOfData, 0, endIndex);
//System.out.println(append.toString());
//System.out.println(stringBufferOfData.toString());
                    System.out.println(s.toString());
                    out[i] = s.toString();
                    i++;

                } catch (Exception exce) {
                }


            }
            for (int j = 0; out[j] != null; j++) {
//String startmessage = "<wsdl:message name=\"" + s[i] + "Response\">";
                System.out.print("sssearch" + out[j]);
            }
            return out;
        }
    }
}
/*FileReader reader = null;
String out[];
out = new String[100];
int i = 0;
try {
//String dir = "File";
reader = new FileReader(new File(fileName));
} catch (FileNotFoundException ex) {
}
BufferedReader bufferReader = new BufferedReader(reader);

Scanner fileToRead = null;
StringBuffer stringBufferOfData = new StringBuffer();

int Index = 0, startIndex = 0, endIndex = 0, bindingIndex = 1;
try {
fileToRead = new Scanner(new File(fileName)); //point the scanner method to a file
//check if there is a next line and it is not null and then read it in
for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
stringBufferOfData.append(line).append("\r\n");//this small line here is to appened all text read in from the file to a string buffer which will be used to edit the contents of the file
}
fileToRead.close();//this is used to release the scanner from file
} catch (FileNotFoundException ex) {
}

String start = "<wsdl:operation name=\"";
String end = "\">";
startIndex = stringBufferOfData.indexOf(start, endIndex);
String binding = "<soap:";
bindingIndex = stringBufferOfData.indexOf(binding);
System.out.println(bindingIndex);
String operation = "";
while ((bufferReader.readLine()) != null && endIndex != -1 && startIndex != -1 && (bindingIndex > startIndex)) {
try {
//now we get the starting point of the text we want to edit
Index = startIndex + start.length();
//System.out.println(startIndex);
//System.out.println(Index);
endIndex = stringBufferOfData.indexOf(end, startIndex);//now we add the staring index of the text with text length to get the end index
//System.out.println(endIndex);
//stringBufferOfData.replace(startIndex, endIndex, replacementText);
CharSequence s = stringBufferOfData.subSequence(Index, endIndex);
startIndex = stringBufferOfData.indexOf(start, endIndex);//now we get the starting point of the text we want to edit

//StringBuffer append = stringBufferOfData.append(stringBufferOfData, 0, endIndex);
//System.out.println(append.toString());
//System.out.println(stringBufferOfData.toString());
System.out.println(s.toString());
out[i] = s.toString();
i++;

} catch (Exception e) {
}


}
for (int j = 0; out[j] != null ; j++) {
//String startmessage = "<wsdl:message name=\"" + s[i] + "Response\">";
System.out.print("sssearch" + out[j]);
}
return out;
}
}*/
