package nl.ctmm.trait.proteomics.ephrin.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import nl.ctmm.trait.proteomics.ephrin.input.ProjectRecordUnit;
import nl.ctmm.trait.proteomics.ephrin.utils.Constants;

/**
 * Write resutls from the table to SummaryFile in Tab delimited format
 * @author opl
 *
 */

public class SummaryFileWriter {
	private static SummaryFileWriter instance = new SummaryFileWriter();
    final File summaryFile = new File(Constants.PROPERTY_SUMMARY_FILE_FULLPATH);
	
    public static SummaryFileWriter getInstance() {
    	if (instance == null) {
    		instance = new SummaryFileWriter();
    	}
      return instance;
    }

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
