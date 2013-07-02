package nl.ctmm.trait.proteomics.ephrin.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import nl.ctmm.trait.proteomics.ephrin.utils.Constants;

public class SummaryFileReader {
	private static SummaryFileReader instance = new SummaryFileReader();
    final File summaryFile = new File(Constants.PROPERTY_SUMMARY_FILE_FULLPATH);
	ArrayList<ProjectRecordUnit> projectRecordUnits = new ArrayList<ProjectRecordUnit>();
    
    public static SummaryFileReader getInstance() {
    	if (instance == null) {
    		instance = new SummaryFileReader();
    	}
      return instance;
    }
    
    public ArrayList<ProjectRecordUnit> retrieveProjectRecords() {
    	if (projectRecordUnits != null) {
    		projectRecordUnits.clear();
    	}
    	int recordNum = 0; 
	    try {
	       InputStreamReader streamReader = new InputStreamReader(new FileInputStream(summaryFile));
	       BufferedReader br = new BufferedReader(streamReader);
	       if (br.ready()) {
	    	   String firstLine = br.readLine(); //Skip first line
	    	   System.out.println("FirstLine = " + firstLine);
	       }
	       //Also check for empty lines and white spaces
	       while (br.ready()) {
	    	   String record = br.readLine();
	    	   record = record.trim();
	    	   System.out.println("record = " + record);
	           if (record.length() > 0) {
	        	   StringTokenizer stkz = new StringTokenizer(record, "\t"); 
	        	   ProjectRecordUnit prUnit = new ProjectRecordUnit(++recordNum, stkz.nextToken(), 
	        			   stkz.nextToken(), stkz.nextToken());
	        	   projectRecordUnits.add(prUnit);
	           }
	       }
	       br.close();
	       streamReader.close(); 
	    } catch (Exception e) {
	        System.out.println(e.toString());
	    }
    	return projectRecordUnits;
    }
}

/**
 * File: EphrinSUmmaryFile.tsv

ProjectName	FirstRawFileRecord	FOlderPath
QE1_130212_OPL00	001QE1_130212_OPL0000_jurkat2ug_01|1|L01	Z:\\qe-raw-data\\sequences
QE2_130219_OPL0000	QE2_130219_OPL0000_jurkat2ug_02|1|L04	C:\\Xcalibur\\methods\\nanoTune
QE2_130225_OPL0000	QE2_130225_OPL0000_jurkat2ug_02|1|L25	C:\\Xcalibur\\methods\\nanoTune\\ESI_POS_S-lens45
QE2_130415_OPL0000	QE2_130415_OPL0000_jurkat2ug_01|1|L27	C:\\Xcalibur\\methods\\nanoTune\\mstune
QE1_130226_OPL0000	QE2_130415_OPL0000_jurkat2ug_01|1|L26	C:\\qc-data\\QCArchive27Feb\\archive

*/