package nl.ctmm.trait.proteomics.ephrin.input;

import java.io.File;
import java.util.ArrayList;

import nl.ctmm.trait.proteomics.ephrin.output.SummaryFileWriter;
import nl.ctmm.trait.proteomics.ephrin.utils.Constants;
/**
 * Parse the txt directory to create project record unit 
 * @author opl
 *
 */
public class TxtDirectoryReader {
    
	private static TxtDirectoryReader instance = new TxtDirectoryReader(); 
	private static SummaryFileReader sfrInstance = SummaryFileReader.getInstance(); 
	/**
	 * Get instance of TxtDirectoryReader
	 * @return instance of TxtDirectoryReader
	 */
	public static TxtDirectoryReader getInstance() {
    	if (instance == null) {
    		instance = new TxtDirectoryReader();
    	}
      return instance;
    }
	
	/**
	 * Retrieve project record from the txt directory
	 * @param txtDirectoryName
	 * @return ArrayList containing project record unit
	 */
	
    public ProjectRecordUnit RetrieveProjectRecord (String txtDirectoryName) {
        final File txtDirectory = new File(txtDirectoryName);
        ProjectRecordUnit prUnit = null; 
        if (txtDirectory.exists()) {
            File txtParent = txtDirectory.getParentFile(); 
            File txtGrandpa = txtParent.getParentFile();
            String projectName = txtGrandpa.getParentFile().getName();
            String projectFolder = txtGrandpa.getParentFile().getAbsolutePath();
            String templateFilePath = "";
            String secondLine = "";
            final File[] files = txtDirectory.listFiles();
            if (files != null) {
                for (final File file : files) {
                   if (file.getName().equals(Constants.PROPERTY_TEMPLATE_FILE_NAME)) {
                       System.out.println("Template file found: " + file.getName() + 
                               " Absolute path: " + file.getAbsolutePath());
                       templateFilePath = file.getAbsolutePath();
                       TemplateFileReader templateFileReader = new TemplateFileReader(templateFilePath);
                       secondLine = templateFileReader.readSecondLine();
                       break;
                   }
                }
            }
            if (secondLine.equals("")) {
            	secondLine = "Invalid - No Template file found.";
            }
            System.out.println("Record: " + projectName + " " + secondLine + " " 
                    + projectFolder);
            
            ArrayList<String> parameterValues = new ArrayList<String>();
            parameterValues.add(projectName);
            parameterValues.add(secondLine);
            parameterValues.add(projectFolder);
            parameterValues.add(Constants.CATEGORY_UNKNOWN);
            parameterValues.add(Constants.NO_COMMENTS_TXT);
            prUnit = new ProjectRecordUnit(-1, parameterValues, sfrInstance.getSortOptionsNames(), sfrInstance.getCategories());
        }
        return prUnit;
    }
}
