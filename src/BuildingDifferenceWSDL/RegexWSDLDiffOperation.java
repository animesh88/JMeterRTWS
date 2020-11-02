/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BuildingDifferenceWSDL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.*;

/**
 *
 * @author ani
 */
public class RegexWSDLDiffOperation {

    public static void main(String[] args) throws IOException {
        String[] Difference = null;
        File WSDL1 = new File("C:\\Users\\ani\\Desktop\\RTST\\SaaS_2.xml");
        File WSDL2 = new File("C:\\Users\\ani\\Desktop\\RTST\\SaaS_3.xml");
        String[] WSDL1Op = OperationAvailable(WSDL1);
        String[] WSDL2Op = OperationAvailable(WSDL2);
        RegexWSDLDiffOperation obj = new RegexWSDLDiffOperation();
        Difference = obj.DiffOperation(WSDL1Op, WSDL2Op);
        for (int i = 0; i < Difference.length && Difference[i] != null; i++) {

            System.out.println("Difference " + Difference[i]);
        }
    }

    public static String[] OperationAvailable(File file) {
        Scanner fileToRead = null;
        StringBuffer stringBufferOfData = new StringBuffer();

        try {
            fileToRead = new Scanner(file);
            for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
                stringBufferOfData.append(line).append("\r\n");
            }
            fileToRead.close();
        } catch (FileNotFoundException ex) {
        }

        String Regex;
        String str;

        Regex = "<operation ([ _A-Za-z0-9-]+)?name((\\s)+)?=((\\s)+)?\"[A-Za-z_0-9]+\"([ _A-Za-z0-9-]+)?>((\\s)+)?<((\\s)+)?input";
        str = stringBufferOfData.toString();
        String TrimInfo = regex(Regex, str);
        System.out.println("\n\nTrim Info " + TrimInfo);
        Regex = "=((\\s)+)?\"[A-Za-z_0-9]+\"((\\s)+)?";
        String OperationTrim = regex(Regex, TrimInfo);
        Regex = "[A-Za-z_0-9]+";
        String Operation[];
        Operation = regexOperation(Regex, OperationTrim);
        for (int i = 0; i < Operation.length && Operation[i] != null; i++) {
            String string = Operation[i];
            System.out.println("\n\nOperations " + string);
        }
        return Operation;
    }

    public static String regex(String Regex, String str) {
        Pattern checker = Pattern.compile(Regex);
        Matcher Matcher = checker.matcher(str);
        String trimInfo = "";

        while (Matcher.find()) {

            if (Matcher.group().length() != 0) {
                trimInfo = trimInfo.concat(Matcher.group().trim());
                System.out.println("\nStr:  " + Matcher.group().trim());
            }
            //System.out.println("Start Indexing of str " + Matcher.start());
            //System.out.println("End Indexing of str" + Matcher.end());
            //System.out.println("Length of str " + Matcher.group().length());
        }
        return trimInfo;
    }

    public static String[] regexOperation(String Regex, String str) {
        Pattern checker = Pattern.compile(Regex);
        Matcher Matcher = checker.matcher(str);
        String trimInfo[] = new String[100];
        int i = 0;
        while (Matcher.find()) {

            if (Matcher.group().length() != 0) {
                trimInfo[i] = Matcher.group().trim();
                System.out.println("\nStr:  " + Matcher.group().trim());
                i++;
            }

            //System.out.println("Start Indexing of str " + Matcher.start());
            //System.out.println("End Indexing of str" + Matcher.end());
            //System.out.println("Length of str " + Matcher.group().length());
        }
        return trimInfo;
    }

    public String[] DiffOperation(String WSDL1Op[], String WSDL2Op[]) {
        String[] DiffOp = new String[100];
        String[] EqualOp = new String[100];
        System.out.print("inside");
        int k = 0, j;

        for (int i = 0; WSDL2Op[i] != null; i++) {
            boolean equal = false;
            for (j = 0; WSDL1Op[j] != null; j++) {
                System.out.println(" WSDL1Op  " + i + WSDL1Op[j] + " WSDL2Op " + j + WSDL2Op[i]);
                if( WSDL1Op[j].equals(WSDL2Op[i]))
                {equal = true;}

            }
            if (equal) {
            } else {
                System.out.println( "INSIDE 2222 :::    DiffOp  "+  i + WSDL2Op[i] );
                DiffOp[k] = WSDL2Op[i];
                System.out.println(k + "B " + DiffOp[k]);
                k++;
            }
        }

        return DiffOp;
    }

    String[] Diff(File WSDL1, File WSDL2) {
        String[] Difference = null;
        String[] WSDL1Op = OperationAvailable(WSDL1);
        String[] WSDL2Op = OperationAvailable(WSDL2);

        Difference = DiffOperation(WSDL1Op, WSDL2Op);

        return Difference;
    }
}

