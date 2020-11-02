/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BuildingDifferenceWSDL;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author ani
 */
public class Construction {

    public static void main(String[] args) throws IOException {

        String newWSDLFileName = "C:\\Users\\ani\\Desktop\\RTST\\Eucalyptus new WSDL.xml";
        File WSDL2 = new File(newWSDLFileName);
        String oldWSDLFileName = "C:\\Users\\ani\\Desktop\\RTST\\Eucalyptus old WSDL.xml";
        File WSDL1 = new File(oldWSDLFileName);
        //String[] s = {"DescribeSensors", "BundleRestartInstance"};

        WSDLDiff objDiff = new WSDLDiff();
        String[] Difference = objDiff.Diff(WSDL1, WSDL2);
        String[] Diffop = objDiff.DiffOperation(Difference);

        //Clean clean = new Clean();
        //clean.cleanPatch(patchFileName);

        StartDefinition obj = new StartDefinition();
        String x = obj.buildDefinition(WSDL2);

        BuildMessage objmsg = new BuildMessage();
        String message = objmsg.buildMessage(WSDL2, Diffop);
        x = x.concat(message);

        BuildPortType objoperation = new BuildPortType();
        String strPort = objoperation.buildportType(WSDL2, Diffop);
        x = x.concat("\n" + strPort);

        BuildBinding objBinding = new BuildBinding();
        String strBinding = objBinding.buildBinding(WSDL2, Diffop);
        x = x.concat("\n" + strBinding);

        EndServiceDefinition objEnd = new EndServiceDefinition();
        String strEnd = objEnd.buildDefinition(WSDL2);
        x = x.concat("\n" + strEnd);

        try {
            String fileName1 = "C:\\Users\\ani\\Desktop\\RTST\\ReducedWSDL.xml";
            //fileName1 = DifferenceWSDL.getPath();
            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileName1));
            bufwriter.write(x);//writes the edited string buffer to the new file
            bufwriter.close();//closes the file
        } catch (Exception e) {//if an exception occurs
        }
    }

    public String[] ConstructDIfferenceWSDL(File WSDL1, File WSDL2, File DifferenceWSDL) throws IOException {
        String[] Diffop = null;
        try {
            //String newWSDLFileName = "C:\\Users\\ani\\Desktop\\AfterGS1\\Eucalyptus 1 month ago.xml";
            //String patchFileName = "C:\\Users\\ani\\Desktop\\AfterGS1\\EucalyptusPatch.patch";
            //String[] s = {"DescribeSensors", "BundleRestartInstance"};
            WSDLDiff objDiff = new WSDLDiff();
            String[] Difference = objDiff.Diff(WSDL1, WSDL2);
            Diffop = objDiff.DiffOperation(Difference);


            //Clean clean = new Clean();
            //clean.cleanPatch(patchFileName);

            StartDefinition obj = new StartDefinition();
            String x = obj.buildDefinition(WSDL2);

            BuildMessage objmsg = new BuildMessage();
            String message = objmsg.buildMessage(WSDL2, Diffop);
            x = x.concat(message);

            BuildPortType objoperation = new BuildPortType();
            String strPort = objoperation.buildportType(WSDL2, Diffop);
            x = x.concat("\n" + strPort);

            BuildBinding objBinding = new BuildBinding();
            String strBinding = objBinding.buildBinding(WSDL2, Diffop);
            x = x.concat("\n" + strBinding);

            EndServiceDefinition objEnd = new EndServiceDefinition();
            String strEnd = objEnd.buildDefinition(WSDL2);
            x = x.concat("\n" + strEnd);

            try {
                String fileName1; //= "C:\\Users\\ani\\Desktop\\After GS1\\ReduceWSDLEucalyptus.xml";
                fileName1 = DifferenceWSDL.getPath();
                BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileName1));
                bufwriter.write(x);//writes the edited string buffer to the new file
                bufwriter.close();//closes the file
            } catch (Exception e) {//if an exception occurs
            }

        } catch (Exception e) {
            RegexWSDLDiffOperation objDiff = new RegexWSDLDiffOperation();
            Diffop = objDiff.Diff(WSDL1, WSDL2);

            //Clean clean = new Clean();
            //clean.cleanPatch(patchFileName);

            StartDefinition obj = new StartDefinition();
            String x = obj.buildDefinition(WSDL2);

            BuildMessage objmsg = new BuildMessage();
            String message = objmsg.buildMessage(WSDL2, Diffop);
            x = x.concat(message);

            BuildPortType objoperation = new BuildPortType();
            String strPort = objoperation.buildportType(WSDL2, Diffop);
            x = x.concat("\n" + strPort);

            BuildBinding objBinding = new BuildBinding();
            String strBinding = objBinding.buildBinding(WSDL2, Diffop);
            x = x.concat("\n" + strBinding);

            EndServiceDefinition objEnd = new EndServiceDefinition();
            String strEnd = objEnd.buildDefinition(WSDL2);
            x = x.concat("\n" + strEnd);

            try {
                String fileName1; //= "C:\\Users\\ani\\Desktop\\After GS1\\ReduceWSDLEucalyptus.xml";
                fileName1 = DifferenceWSDL.getPath();
                BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileName1));
                bufwriter.write(x);//writes the edited string buffer to the new file
                bufwriter.close();//closes the file
            } catch (Exception ex) {//if an exception occurs
            }
        }

        return Diffop;
    }
}
