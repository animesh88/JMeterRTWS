/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ParseJMeterTS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
public class Construction {

    public static void main(String arg[]) {
        String buffer = "";
        ArrayList<String> a = new ArrayList<String>();
        a.add("Add");
        String[] s = new String[100];
        s[0] = "Searching";
        s[1] = "readingFile";

        try {
            BufferedReader br = new BufferedReader(new FileReader("D:\\others\\RTST\\JMeter\\SearchingAsAService.jmx"));
            String sCurrentLine;
            Boolean regexFlag = false;
            Boolean caseFlag = false;
            while ((sCurrentLine = br.readLine()) != null) {

                if (sCurrentLine.contains("testname=\"") && sCurrentLine.contains("<SoapSampler ")) {
                    regexFlag = true;
                }

                if (regexFlag == false) {
                    buffer = buffer + sCurrentLine + "\n";
                } else {
                    for (int i = 0; s[i] != null; i++) {
                        if (sCurrentLine.contains(s[i])) {
                            if (sCurrentLine.contains("testname=\"" + s[i] + "\"")) {
                                caseFlag = true;
                            }
                        }
                    }
                    if (caseFlag == true) {
                        if (sCurrentLine.contains("</SoapSampler>")) {
                            regexFlag = false;
                            caseFlag = false;
                        }
                        buffer = buffer + sCurrentLine + "\n";
                    } else {
                        caseFlag = false;
                        if (sCurrentLine.contains("<hashTree/>")) {
                            regexFlag = false;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(buffer);

//        String x = "Add";
//        System.out.println(buffer);
//        String startRegex = "<SoapSampler [ a-zA-Z=\"-]+ testname=\"" + x + "\" [ a-zA-Z=\"-]+>";
//        String endRegex = "</SoapSampler>";
//        String soapSampler = regexString(buffer, startRegex, endRegex);
//        System.out.println(soapSampler);

    }

    private static String regexString(String buffer, String startRegex, String endRegex) {
        int startMatcherIndex = 0, endMatcherIndex = 0;
        Pattern startPattern = Pattern.compile(startRegex);
        Matcher startMatcher = startPattern.matcher(buffer);
        // using Matcher find(), group(), start() and end() methods
        while (startMatcher.find()) {
            System.out.println("Found the text \"" + startMatcher.group()
                    + "\" starting at " + startMatcher.start()
                    + " index and ending at index " + startMatcher.end());
            startMatcherIndex = startMatcher.start();
        }

        Pattern endPattern = Pattern.compile(endRegex);
        Matcher endMatcher = endPattern.matcher(buffer);
        // using Matcher find(), group(), start() and end() methods
        while (endMatcher.find() && endMatcherIndex == 0) {
            System.out.println("Found the text \"" + endMatcher.group()
                    + "\" starting at " + endMatcher.start()
                    + " index and ending at index " + endMatcher.end());
            endMatcherIndex = endMatcher.end();
        }

        String subString = buffer.substring(startMatcherIndex, endMatcherIndex);
        return subString;
    }

    public void ConstructReduceTC(File testSuiteFile1, String[] s, File ReduceTS) {
        String buffer = "";
        ArrayList<String> a = new ArrayList<String>();
        a.add("Add");

        try {
            BufferedReader br = new BufferedReader(new FileReader(testSuiteFile1));
            String sCurrentLine;
            Boolean regexFlag = false;
            Boolean caseFlag = false;
            while ((sCurrentLine = br.readLine()) != null) {

                if (sCurrentLine.contains("testname=\"") && sCurrentLine.contains("<SoapSampler ")) {
                    regexFlag = true;
                }

                if (regexFlag == false) {
                    buffer = buffer + sCurrentLine + "\n";
                } else {

                    for (int i = 0; s[i] != null; i++) {
                        if (sCurrentLine.contains(s[i])) {
                            if (sCurrentLine.contains("testname=\"" + s[i] + "\"")) {
                                caseFlag = true;
                            }
                        }
                    }
                    if (caseFlag == true) {
                        if (sCurrentLine.contains("</SoapSampler>")) {
                            regexFlag = false;
                            caseFlag = false;
                        }
                        buffer = buffer + sCurrentLine + "\n";
                    } else {
                        caseFlag = false;
                        if (sCurrentLine.contains("<hashTree/>")) {
                            regexFlag = false;
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(buffer);

        try {
            //String fileName1 = "C:\\Users\\ani\\Desktop\\AfterGS1\\Reduced testSuite.xml";
            String fileName1 = ReduceTS.getAbsolutePath();
            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileName1));
            bufwriter.write(buffer.toString());//writes the edited string buffer to the new file
            bufwriter.close();//closes the file
        } catch (Exception e) {//if an exception occurs
        }

//        String x = "Add";
//        System.out.println(buffer);
//        String startRegex = "<SoapSampler [ a-zA-Z=\"-]+ testname=\"" + x + "\" [ a-zA-Z=\"-]+>";
//        String endRegex = "</SoapSampler>";
//        String soapSampler = regexString(buffer, startRegex, endRegex);
//        System.out.println(soapSampler);

    }

    public String[][] testCases(File testSuiteFile4) {
        String TC[][] = new String[100][100];

        Document dom = null;
        Construction obj = new Construction();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            //parse using builder to get DOM representation of the XML file
            dom = db.parse(testSuiteFile4);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        NodeList nodeList = null, innernodeList = null;
        //Element docEle = dom.getDocumentElement();

        NodeList nl = dom.getElementsByTagName("con:testSuite");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                Element e1 = (Element) nl.item(i);
                if (e1.getNodeType() == Node.ELEMENT_NODE) {

                    nodeList = e1.getElementsByTagName("con:testCase");

                    if (nodeList != null && nodeList.getLength() > 0) {
                        for (int j = 0; j < nodeList.getLength(); j++) {
                            Element e2 = (Element) nodeList.item(j);
                            String testCaseName = obj.getNodeAttr("name", e2);
                            System.out.println("Test case name    " + testCaseName);
                            TC[j][0] = testCaseName;
                            //System.out.println("T C    " + TC[j]);
                            //important code for Reading test  Step Name

                            innernodeList = e2.getElementsByTagName("con:testStep");

                            if (innernodeList != null && innernodeList.getLength() > 0) {
                                for (int k = 0; k < innernodeList.getLength(); k++) {
                                    Element e3 = (Element) innernodeList.item(k);
                                    //Node exec = obj.getNode("name", e3.getChildNodes());
                                    String testStepName = obj.getNodeAttr("name", e3);
                                    TC[j][k + 1] = testStepName;
                                    System.out.println("Test Step name    " + testStepName);
                                }
                            }
                        }

                    }
                }
            }
        }

        return TC;
    }

    protected Node getNode(String tagName, NodeList nodes) {
        for (int x = 0; x
                < nodes.getLength(); x++) {
            Node node = nodes.item(x);


            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;


            }
        }

        return null;


    }

    protected String getNodeValue(Node node) {
        NodeList childNodes = node.getChildNodes();


        for (int x = 0; x
                < childNodes.getLength(); x++) {
            Node data = childNodes.item(x);


            if (data.getNodeType() == Node.TEXT_NODE) {
                return data.getNodeValue();


            }
        }
        return "";


    }

    protected String getNodeValue(String tagName, NodeList nodes) {
        for (int x = 0; x
                < nodes.getLength(); x++) {
            Node node = nodes.item(x);


            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();


                for (int y = 0; y
                        < childNodes.getLength(); y++) {
                    Node data = childNodes.item(y);


                    if (data.getNodeType() == Node.TEXT_NODE) {
                        return data.getNodeValue();


                    }
                }
            }
        }
        return "";
    }

    protected String getNodeAttr(String attrName, Node node) {
        NamedNodeMap attrs = node.getAttributes();
        for (int y = 0; y
                < attrs.getLength(); y++) {
            Node attr = attrs.item(y);


            if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                return attr.getNodeValue();


            }
        }
        return "";


    }

    protected String getNodeAttr(String tagName, String attrName, NodeList nodes) {
        for (int x = 0; x
                < nodes.getLength(); x++) {
            Node node = nodes.item(x);


            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();


                for (int y = 0; y
                        < childNodes.getLength(); y++) {
                    Node data = childNodes.item(y);


                    if (data.getNodeType() == Node.ATTRIBUTE_NODE) {
                        if (data.getNodeName().equalsIgnoreCase(attrName)) {
                            return data.getNodeValue();


                        }
                    }
                }
            }
        }

        return "";

    }

    public void ConstructReduceTC(File testSuiteFile4, String TestCase, File ReduceTS) {
//       Start start = new Start();
//        CharSequence x = start.buildStart(testSuiteFile);
//
//        TestCases TC = new TestCases();
//        System.out.println("Test Cases " + TestCase);
//        x = x.toString().concat(TestCase);
//
//        x = x.toString().concat("<con:properties/><con:reportParameters/></con:testSuite>");
//
//        try {
//            //String fileName1 = "C:\\Users\\ani\\Desktop\\AfterGS1\\Reduced testSuite.xml";
//            String fileName1 = ReduceTS.getAbsolutePath();
//            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileName1));
//            bufwriter.write(x.toString());//writes the edited string buffer to the new file
//            bufwriter.close();//closes the file
//        } catch (Exception e) {//if an exception occurs
//        }
    }

    public String buildTestCase(File testSuiteFile4, String[] buildTestCase) {
        String x = "";
        String TestCase = "";
//        String start = "<con:testCase failOnError=\"true\" failTestCaseOnErrors=\"true\" keepSession=\"false\" maxResults=\"0\" name=\"" + s[0] + "\" searchProperties=\"true\"><con:settings/>";
//        String end = "<con:properties/><con:reportParameters/></con:testCase>";
//        for (int i = 1; i < s.length && s[i] != null; i++) {
//            StringBuffer stringBufferOfData = new StringBuffer();
//            String startTestStep = "<con:testStep type=\"request\" name=\""+ s[i] +"\">";
//            String endTestStep = "</con:testStep>";
//            Scanner fileToRead = null;
//            fileToRead = new Scanner(fileName); //point the scanner method to a file
//            //check if there is a next line and it is not null and then read it in
//            for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
//                stringBufferOfData.append(line).append("\r\n");
//            }
//            int startTSIndex = stringBufferOfData.indexOf(startTestStep);//now we get the starting point of the text we want to edit
//            System.out.println("start" + startTSIndex);
//            int endTSIndex = stringBufferOfData.indexOf(endTestStep, startTSIndex);//now we get the starting point of the text we want to edit
//            System.out.println("end " + endTSIndex);
//            //int endIndex = startIndex + lineToEdit.length();//now we add the staring index of the text with text length to get the end index
//            CharSequence teststep = stringBufferOfData.subSequence(startTSIndex, endTSIndex).toString();
//
//            teststep = teststep.toString().concat(endTestStep);
//
//            x = x.concat(teststep.toString());
//        }
//        TestCase = TestCase.concat(start).concat(x).concat(end);
//
        return TestCase;
    }
}
