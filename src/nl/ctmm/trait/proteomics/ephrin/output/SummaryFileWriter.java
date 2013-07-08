package nl.ctmm.trait.proteomics.ephrin.output;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import nl.ctmm.trait.proteomics.ephrin.input.ProjectRecordUnit;
import nl.ctmm.trait.proteomics.ephrin.utils.Constants;

/**
 * Write results from the table to SummaryFile in Tab delimited format
 * @author opl
 *
 */

public class SummaryFileWriter {
	private static SummaryFileWriter instance = new SummaryFileWriter();
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
        	//overwrite project records
            FileWriter fWriter = new FileWriter(summaryFile, false);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            bWriter.write("ProjectName\tFirstRawFileRecord\tFOlderPath\n");
            for (int i = 0; i < projectRecordUnits.size(); ++i) {
            	ProjectRecordUnit thisUnit = projectRecordUnits.get(i);
            	bWriter.write(thisUnit.getProjectName() + "\t" + 
            			thisUnit.getFirstRawFile() + "\t" + thisUnit.getFolderPath() + "\t" + 
            			thisUnit.getCategory() + "\t" + thisUnit.getComment() + "\n");
            }            
            bWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false; 
        }
        return true;
    }
    
    /**
     * Append project record unit to the end of EphrinSummaryFile.tsv
     * @param projectRecordUnit project record unit to be appended
     */
	public void addProjectRecordUnit(ProjectRecordUnit projectRecordUnit) {
        try {
            FileWriter fWriter = new FileWriter(summaryFile, true);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            bWriter.write(projectRecordUnit.getProjectName() + "\t" + 
            		projectRecordUnit.getFirstRawFile() + "\t" + projectRecordUnit.getFolderPath() + "\n");
            bWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
