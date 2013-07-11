package nl.ctmm.trait.proteomics.ephrin;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import nl.ctmm.trait.proteomics.ephrin.gui.ViewerFrame;
import nl.ctmm.trait.proteomics.ephrin.input.ProjectRecordUnit;
import nl.ctmm.trait.proteomics.ephrin.input.SummaryFileReader;
import nl.ctmm.trait.proteomics.ephrin.input.TxtDirectoryReader;
import nl.ctmm.trait.proteomics.ephrin.output.SummaryFileWriter;
import nl.ctmm.trait.proteomics.ephrin.utils.Constants;

public class Main {
	private ViewerFrame viewerFrame;
    private SummaryFileWriter summaryFileWriter; 
    private SummaryFileReader summaryFileReader; 

    private static Main instance = new Main();
    
    /**
     * Gets the Main instance.
     * 
     * @return Main instance
     */
    public static Main getInstance() {
    	if (instance == null) {
    		instance = new Main();
    	}
      return instance;
    }
    
    /**
     * Create and show ViewerFrame GUI
     * @param appProperties
     */
    private void createAndShowGUI(Properties appProperties) {
        //Create and set up the window.
        viewerFrame = new ViewerFrame("Ephrin - Proteomics Project Tracker", instance, retrieveSortOptions(), retrieveCategories(), retrieveRecordUnits());
    }

	/**
     * Load the application properties from the properties file.
     *
     * @return the application properties.
     */
    private Properties loadProperties() {
        final Properties appProperties = new Properties();
        // Load actual properties from file.
        try {
            final FileInputStream fileInputStream = new FileInputStream(Constants.PROPERTIES_FILE_NAME);
            appProperties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("Loading of application properties failed." + e);
        }
        return appProperties;
    }
    
	/**
	 * @param args
	 */
    public static void main(String[] args) {
    	Main instance = Main.getInstance();
    	instance.createAndShowGUI(instance.loadProperties());
    }

    /**
     * Retrieve project record units from the EphrinSummaryFile.tsv
     * @return ArrayList of project record units
     */
    private ArrayList<ProjectRecordUnit> retrieveRecordUnits() {
    	ArrayList<ProjectRecordUnit> recordUnits = new ArrayList<ProjectRecordUnit>();
    	summaryFileReader = SummaryFileReader.getInstance();
    	recordUnits = summaryFileReader.retrieveProjectRecords();
    	return recordUnits;
    }
    
    /**
     * Retrieve sort options from the EphrinSummaryFile.tsv
     * @return ArrayList of sort options
     */
    private ArrayList<String> retrieveSortOptions() {
    	ArrayList<String> sortOptions = new ArrayList<String>();
    	summaryFileReader = SummaryFileReader.getInstance();
    	sortOptions = summaryFileReader.getSortOptionsNames();
    	return sortOptions;
    }
    
    /**
     * Retrieve categories from the EphrinSummaryFile.tsv
     * @return ArrayList of categories
     */
    private ArrayList<String> retrieveCategories() {
    	ArrayList<String> categories = new ArrayList<String>();
    	summaryFileReader = SummaryFileReader.getInstance();
    	categories = summaryFileReader.getCategories();
    	return categories;
    }
    
    /**
     * Overwrite displayed project records in the ViewerFrame. 
     * The reason behind overwriting is changes to the EphrinSummaryFile.tsv file.  
     *
     * @param projectRecordUnits
     */
    public void notifyOverwriteProjectRecords(ArrayList<ProjectRecordUnit> projectRecordUnits) {
    	System.out.println("Main::notifyOverwriteProjectRecords " + projectRecordUnits.size() + " records");
    	summaryFileWriter = SummaryFileWriter.getInstance();
    	if (summaryFileWriter.OverwriteProjectRecords(projectRecordUnits)) {
    		viewerFrame.overwriteRecordUnits(retrieveRecordUnits());
    		viewerFrame.updateEphrinStatus(projectRecordUnits.size() + " records successfully saved to " + Constants.PROPERTY_SUMMARY_FILE_FULLPATH, false);
    	} else {
    		viewerFrame.updateEphrinStatus("Alert!! Something went wrong while saving project records!!", true);
    	}
    }
    
    /**
     * A new txt directory is chosen by the end-user. Read project records and update ViewerFrame
     * @param txtDirectoryName
     */
	public void notifyNewTxtDirectorySelected(String txtDirectoryName) {
		System.out.println("Main::notifyNewTxtDirectorySelected " + txtDirectoryName);
	    TxtDirectoryReader txtDirectoryReader = TxtDirectoryReader.getInstance(); 
	    ProjectRecordUnit prUnit = txtDirectoryReader.RetrieveProjectRecord(txtDirectoryName);
	    if (prUnit != null) {
		    System.out.println("New record found in " + txtDirectoryName);
		    viewerFrame.addRecordUnit(prUnit); 
	    } else {
	    	System.out.println("Invalid txt directory: " + txtDirectoryName + ".");
	    	viewerFrame.updateEphrinStatus("Invalid txt directory: " + txtDirectoryName + ".", true); 
	    }

	}

}
