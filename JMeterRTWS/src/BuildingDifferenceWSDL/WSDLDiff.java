/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BuildingDifferenceWSDL;

import java.util.List;
import com.predic8.wsdl.*;
import com.predic8.wsdl.diff.WsdlDiffGenerator;
import com.predic8.soamodel.Difference;
import java.io.File;

/**
 *
 * @author ani
 */
public class WSDLDiff {

    public static void main(String[] args) {
        
        File WSDL1 = new File("C:\\Users\\ani\\Desktop\\AfterGS1\\Eucalyptus 7month ago.xml");
        File WSDL2 = new File("C:\\Users\\ani\\Desktop\\AfterGS1\\Eucalyptus 1 month ago.xml");
        WSDLDiff obj = new WSDLDiff();
        String[] Difference = obj.Diff(WSDL1, WSDL2);
        for (int z = 0; Difference[z] != null; z++) {
            System.out.println(z + " " + Difference[z]);
        }
        String[] DiffOp = obj.DiffOperation(Difference);
        String[] Diffschema = obj.DiffSchema(Difference);
        for (int z = 0; DiffOp[z] != null; z++) {
            System.out.println(z + " " + DiffOp[z]);
        }
        for (int z = 0; Diffschema[z] != null; z++) {
            System.out.println(z + " " + Diffschema[z]);
        }
    }

    public String[] Diff(File WSDL1, File WSDL2) {

        String[] Difference = null;
        
            WSDLParser parser = new WSDLParser();

            Definitions wsdl1 = parser.parse(WSDL1.getPath());

            Definitions wsdl2 = parser.parse(WSDL2.getPath());

            WsdlDiffGenerator diffGen = new WsdlDiffGenerator(wsdl1, wsdl2);

            List<Difference> lst = diffGen.compare();
            WSDLDiff obj = new WSDLDiff();
            for (Difference diff : lst) {
                Difference = obj.dumpDiff(diff, "");
            }
            for (int z = 0; Difference[z] != null; z++) {
                for (int x = z + 1; Difference[x] != null; x++) {
                    if ((Difference[x]).equalsIgnoreCase((Difference[z]))) {
                        Difference[x] = null;
                    }
                }
            }
        
        return Difference;
    }
    int i = 0;
    String[] Diff = new String[100];

    private String[] dumpDiff(Difference diff, String level) {

        System.out.println(level + diff.getDescription());
        WSDLDiff obj = new WSDLDiff();
        String s = obj.RetrievDiff(diff.getDescription());
        if (s != null) {
            Diff[i] = s;
            i++;
        }
        for (Difference localDiff : diff.getDiffs()) {
            dumpDiff(localDiff, level + "  ");
        }

        return Diff;
    }

    private String RetrievDiff(String description) {
        String s = null;
        int beginIndex, endIndex;
        String schema = "Schema";
        if (description.startsWith("Operation") && description.endsWith("added.")) {
            String opStrart = "Operation ";
            String opEnd = " added";
            beginIndex = description.indexOf(opStrart) + opStrart.length();
            endIndex = description.indexOf(opEnd);
            System.out.println(" beginIndex " + beginIndex + " EndIndex " + endIndex);
            s = description.substring(beginIndex, endIndex);
            System.out.println(s);
        }
        if (description.startsWith("Schema")) {
            s = "YYYSchemaYYY";
            System.out.println(s);
        }
        if (description.startsWith("ComplexType") && description.endsWith("ResponseType added.")) {
            String opStrart = "ComplexType ";
            String opEnd = "ResponseType added";
            beginIndex = description.indexOf(opStrart) + opStrart.length();
            endIndex = description.indexOf(opEnd);
            System.out.println(" beginIndex " + beginIndex + " EndIndex " + endIndex);
            s = description.substring(beginIndex, endIndex);
            System.out.println(s);
        } else if (description.startsWith("ComplexType") && description.endsWith("Type added.")) {
            String opStrart = "ComplexType ";
            String opEnd = "Type added";
            beginIndex = description.indexOf(opStrart) + opStrart.length();
            endIndex = description.indexOf(opEnd);
            System.out.println(" beginIndex " + beginIndex + " EndIndex " + endIndex);
            s = description.substring(beginIndex, endIndex);
            System.out.println(s);
        }
        if (description.startsWith("Element") && description.endsWith("Response added.")) {
            String opStrart = "Element ";
            String opEnd = "Response added";
            beginIndex = description.indexOf(opStrart) + opStrart.length();
            endIndex = description.indexOf(opEnd);
            System.out.println(" beginIndex " + beginIndex + " EndIndex " + endIndex);
            s = description.substring(beginIndex, endIndex);
            System.out.println(s);
        } else if (description.startsWith("Element") && description.endsWith(" added.")) {
            String opStrart = "Element ";
            String opEnd = " added";
            beginIndex = description.indexOf(opStrart) + opStrart.length();
            endIndex = description.indexOf(opEnd);
            System.out.println(" beginIndex " + beginIndex + " EndIndex " + endIndex);
            s = description.substring(beginIndex, endIndex);
            System.out.println(s);
        }

        return s;
    }

    public String[] DiffOperation(String[] Difference) {
        String[] DiffOp = new String[100];
        int z = 0;
        for (int y = 0; Difference[z] == null || Difference[z] != ("YYYSchemaYYY"); z++, y++) {
            DiffOp[y] = Difference[z];
            System.out.println(z + " " + DiffOp[y]);
        }

        return DiffOp;
    }

    public String[] DiffSchema(String[] Difference) {
        String[] DiffSchema = new String[100];
        int z = 0;

        for (; Difference[z] == null || Difference[z] != ("YYYSchemaYYY"); z++) {
            //System.out.println(z + " " + Difference[z]);
        }
        z++;
        //System.out.println(z + " " + DiffSchema[z]);
        for (int y = 0; Difference[z] != null; z++, y++) {
            DiffSchema[y] = Difference[z];
            System.out.println(y + " " + DiffSchema[y]);
        }
        return DiffSchema;
    }
}
