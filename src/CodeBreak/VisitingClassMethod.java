/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CodeBreak;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
 *
 * @author Administrator
 */
public class VisitingClassMethod {

    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        //FileInputStream in = new FileInputStream("C:\\Users\\ani\\Documents\\NetBeansProjects\\JavaCodeParsing\\src\\javacodeparsing\\test.java");
        FileInputStream in1 = new FileInputStream("C:\\Users\\ani\\Documents\\NetBeansProjects\\JavaCodeParsing\\src\\javacodeparsing\\test.java");

        CompilationUnit cu;
        try {
            // parse the file
            //cu = JavaParser.parse(in);
            cu = JavaParser.parse(in1);
            //JavaParser.
        } finally {
            //in.close();
            in1.close();
        }

        // visit and print the methods names
        new MethodVisitor().visit(cu, null);
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes. 
     */
    public static class MethodVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this 
            // CompilationUnit, including inner class methods
            String x= "";
            x = x.concat(n.getName());
            System.out.println("Name  " + x.concat(n.getName()));
            x= x.concat(n.getParameters().toString());
            System.out.println("Parameter   " + x.concat(n.getParameters().toString()));
            System.out.println("Body  " + x.concat(n.getBody().toString()));
            x=x.concat(n.getBody().toString());
            System.out.println("X  " + x);
            // System.out.println("BodyStmt  "  + n.getBody().getStmts());
            //JavaCodeParsing y = new JavaCodeParsing();
            //CompilationUnit cu;
            /*FileInputStream in = null;
            try {
            in = new FileInputStream("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\JavaCodeParsing\\src\\javacodeparsing\\test.java");
            } catch (FileNotFoundException ex) {
            Logger.getLogger(VisitingClassMethod.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Reader
            //BufferedReader a = new BufferedReader();
            // cu = JavaParser.parse(in);
            
            JavaCharStream x = new JavaCharStream(in);
            

            ASTParserTokenManager tokenManager = new ASTParserTokenManager(x);
            while (tokenManager.getNextToken()!=null)
            {System.out.println("Tooken" + tokenManager); //.toString());

            }*/

            //System.out.println(n.getArrayCount());
            //System.out.println(n.equals(arg));
            //System.out.println(n.getModifiers());
            //String n.get;


            try {
            String fileName1 = "C:\\Users\\ani\\Desktop\\RTST\\Java Code\\"+ arg.toString()+ "\\"+n.getName()+".java";
            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(fileName1));
            bufwriter.write(x);//writes the edited string buffer to the new file
            bufwriter.close();//closes the file
            } catch (Exception e) {//if an exception occurs
            }

        }
    }
}
