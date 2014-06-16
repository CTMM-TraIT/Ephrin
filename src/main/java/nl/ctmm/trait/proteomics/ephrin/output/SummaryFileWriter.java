package nl.ctmm.trait.proteomics.ephrin.output;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.ctmm.trait.proteomics.ephrin.input.ProjectRecordUnit;
import nl.ctmm.trait.proteomics.ephrin.input.SummaryFileReader;
import nl.ctmm.trait.proteomics.ephrin.utils.Constants;

/**
 * Write results from the table to SummaryFile in Tab delimited format
 * @author opl
 *
 */

public class SummaryFileWriter {
	private static SummaryFileWriter instance = new SummaryFileWriter();
	private static SummaryFileReader sfrInstance = SummaryFileReader.getInstance(); 
    final File summaryFile = new File(Constants.PROPERTY_SUMMARY_FILE_FULLPATH);
	
    /**
     * Get instance of the SummaryFileWriter
     * @return instance of the SummaryFileWriter
     */
    public static SummaryFileWriter getInstance() {
    	if (instance == null) {
    		instance = new SummaryFileWriter();
    	}
      return instance;
    }

    /**
     * Take backup of EphrinSummaryFile.tsv to EphrinSummaryFile.tsv.bak
     * Overwrite project records in the EphrinSummaryFile.tsv
     *  
     * @param projectRecordUnits
     * @return Flag indicating success/failure
     */
    public boolean OverwriteProjectRecords(ArrayList<ProjectRecordUnit> projectRecordUnits) {
    	System.out.println("SummaryFileWriter::OverwriteProjectRecords " + projectRecordUnits.size() + " records");
        try {
        	Path source = Paths.get(Constants.PROPERTY_SUMMARY_FILE_FULLPATH); 
        	Path destination = Paths.get(Constants.PROPERTY_BACKUP_FILE_FULLPATH);
        	//take backup
        	Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        	//Read first line of summary file
		    InputStreamReader streamReader = new InputStreamReader(new FileInputStream(summaryFile));
		    BufferedReader br = new BufferedReader(streamReader);
		    String firstLine = "";
		    while (br.ready() && firstLine.equals("")) {
		    	firstLine = br.readLine().trim(); //Skip first line
		    	System.out.println("FirstLine = " + firstLine);
		    }
		    br.close();
		    streamReader.close(); 
		    //Write records to the summary file
            FileWriter fWriter = new FileWriter(summaryFile, false);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
        	//Write first line to the summary file
            bWriter.write(firstLine + "\n");
            List<String> paramNames = sfrInstance.getSortOptionsNames();
        	//overwrite project records
            for (int i = 0; i < projectRecordUnits.size(); ++i) {
            	ProjectRecordUnit thisUnit = projectRecordUnits.get(i);
            	for (int j = 0; j < paramNames.size(); ++j) {
            		bWriter.write(thisUnit.getParameterValueFromKey(paramNames.get(j)) + "\t");
            	}
            	bWriter.write("\n");
            }            
            bWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false; 
        }
        return true;
    }

}
