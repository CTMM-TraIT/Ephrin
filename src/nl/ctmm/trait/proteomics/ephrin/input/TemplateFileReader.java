package nl.ctmm.trait.proteomics.ephrin.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Read the experiment design template file experimentalDesignTemplate.txt
 * @author opl
 *
 */
public class TemplateFileReader {
	
	String templateFilePath = "";
	
	/**
	 * Constructor 
	 * @param templateFilePath Path to experimentalDesignTemplate.txt
	 */
	public TemplateFileReader(String templateFilePath) {
		this.templateFilePath = templateFilePath;
	}
	
	/**
	 * Read second line of the experimentalDesignTemplate.txt
	 * @return Second line of the experimentalDesignTemplate.txt
	 */
	public String readSecondLine() {
		 File templateFile = new File(templateFilePath);
	     String secondLine = "";
	     try {
	        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(templateFile));
	        BufferedReader br = new BufferedReader(streamReader);
	        //Also check for empty lines and white spaces
	        int lineNo = 0; 
	        while (br.ready()) {
	        	String thisLine = br.readLine();
	            thisLine = thisLine.trim();
	            if (thisLine.length() > 0) {
	            	++lineNo; //1st line, second line etc.
	                if (lineNo == 2) {
	                  	secondLine = thisLine.replace("\t", "|");
	                   	break;
	                } 
	            }
	        }
	    } catch (Exception e) {
	        System.out.println(e.toString());
	    }
	    System.out.println("Second line = " + secondLine);
	    return secondLine;
	}
}

/**
 * File: experimentalDesignTemplate.txt

Name	Fraction	Experiment
QE1_130212_OPL0000_jurkat2ug_01	1	L01
QE1_130212_OPL0000_jurkat2ug_02	1	L02
QE1_130212_OPL0000_jurkat2ug_03	1	L03
QE1_130212_OPL0000_jurkat2ug_04	1	L04
QE1_130212_OPL0000_jurkat2ug_05	1	L05
QE1_130212_OPL0000_jurkat2ug_06	1	L06
QE1_130212_OPL0000_jurkat2ug_07	1	L07

*/