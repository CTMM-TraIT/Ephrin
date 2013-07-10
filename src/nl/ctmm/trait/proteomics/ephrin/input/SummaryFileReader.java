package nl.ctmm.trait.proteomics.ephrin.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import nl.ctmm.trait.proteomics.ephrin.utils.Constants;

/**
 * Reads the EphrinSummaryFile.tsv
 * @author opl
 *
 */
public class SummaryFileReader {
	private static SummaryFileReader instance = new SummaryFileReader();
    final File summaryFile = new File(Constants.PROPERTY_SUMMARY_FILE_FULLPATH);
	ArrayList<ProjectRecordUnit> projectRecordUnits = new ArrayList<ProjectRecordUnit>();
	ArrayList<String> sortOptionsNames = new ArrayList<String>();
	ArrayList<String> categories = new ArrayList<String>();
	/*
	 * Parsing header values and configuring viewer based on header information
	 * ProjectName	FirstRawFileRecord	FolderPath	Category{Human;Mouse;Unknown}	Comment{}
	 */
	
	/**
	 * Constructor. Parse sort options and categories based on information in the first line
	 * ProjectName	FirstRawFileRecord	FolderPath	Category{Human;Mouse;Unknown}	Comment{}
	 */
    public SummaryFileReader() {
	    try {
		       InputStreamReader streamReader = new InputStreamReader(new FileInputStream(summaryFile));
		       BufferedReader br = new BufferedReader(streamReader);
		       String firstLine = "";
		       while (br.ready() && firstLine.equals("")) {
		    	   firstLine = br.readLine().trim(); //Skip first line
		    	   System.out.println("FirstLine = " + firstLine);
		       }
		       br.close();
		       streamReader.close(); 
		       if (firstLine.length() > 0) {
		    	   parseSortOptionsNames(firstLine); 
		       }
		    } catch (Exception e) {
		        System.out.println(e.toString());
		    }
    }
    
    /**
     * Parse sort options from the first line of EphrinSummaryFile.tsv
     * @param firstLine
     */
    private void parseSortOptionsNames(String firstLine) {
    	StringTokenizer stkz = new StringTokenizer(firstLine, "\t"); 
    	while (stkz.hasMoreTokens()) {
    		String sortOption = stkz.nextToken().trim(); 
    		if (sortOption.startsWith("Category")) {
    			parseCategories(sortOption);
    			sortOptionsNames.add("Category");
    		} else {
    			sortOptionsNames.add(sortOption);
    		}
    	}
    	System.out.println("Following are sort options:");
    	for (int i = 0; i < sortOptionsNames.size(); ++i) {
    		System.out.print(sortOptionsNames.get(i) + " ");
    	}
    	System.out.println();
    }
    
    /**
     * Parse categories from the categoryString
     * @param categoryString
     */
    private void parseCategories(String categoryString) {
    	StringTokenizer stkz = new StringTokenizer(categoryString, "{;}");
    	stkz.nextToken();
    	while (stkz.hasMoreTokens()) {
    		categories.add(stkz.nextToken());
    	}
    	System.out.println("Following are categories:");
    	for (int i = 0; i < categories.size(); ++i) {
    		System.out.print(categories.get(i) + " ");
    	}
    	System.out.println();
    }
    
    /**
     * Get column names and sort options
     * @return sortOptionsNames
     */
    public ArrayList<String> getSortOptionsNames() {
    	return sortOptionsNames;
    }
    
    /**
     * Get categories list e.g. Human, Mouse, Unknown
     * @return categories
     */
    public ArrayList<String> getCategories() {
    	return categories;
    }
    
    
	/**
	 * Get instance of SummaryFileReader
	 * @return instance of SummaryFileReader
	 */
    public static SummaryFileReader getInstance() {
    	if (instance == null) {
    		instance = new SummaryFileReader();
    	}
      return instance;
    }
    
    /**
     * Retrieve project records from the EphrinSummaryFile.tsv
     * @return ArrayList containing project record units
     */
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
	        	   System.out.println("Number of tokens = " + stkz.countTokens());
	        	   ArrayList<String> parameterValues = new ArrayList<String>();
	        	   while (stkz.hasMoreTokens()) {
	        		   parameterValues.add(stkz.nextToken().trim()); 
	        	   }
	        	   ProjectRecordUnit prUnit = new ProjectRecordUnit(++recordNum, parameterValues, sortOptionsNames, categories);
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