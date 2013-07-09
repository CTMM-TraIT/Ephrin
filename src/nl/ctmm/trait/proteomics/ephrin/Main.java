package nl.ctmm.trait.proteomics.ephrin;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;

import nl.ctmm.trait.proteomics.ephrin.gui.ViewerFrame;
import nl.ctmm.trait.proteomics.ephrin.input.ProjectRecordUnit;
import nl.ctmm.trait.proteomics.ephrin.input.SummaryFileReader;
import nl.ctmm.trait.proteomics.ephrin.input.TemplateFileReader;
import nl.ctmm.trait.proteomics.ephrin.input.TxtDirectoryReader;
import nl.ctmm.trait.proteomics.ephrin.output.SummaryFileWriter;
import nl.ctmm.trait.proteomics.ephrin.utils.Constants;

public class Main {
	private Properties applicationProperties = null; 
    private ViewerFrame viewerFrame;
    private TemplateFileReader templateFileReader; 
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
        viewerFrame = new ViewerFrame("Ephrin - Proteomics Project Tracker", instance, Constants.SORT_OPTION_NAMES, retrieveRecordUnits());
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
    	/*
    	//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	instance.createAndShowGUI();
            }
        });
        */
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
     * Overwrite displayed project records in the ViewerFrame. 
     * The reason behind overwriting is changes to the EphrinSummaryFile.tsv file.  
     *
     * @param projectRecordUnits
     */
    public void notifyOverwriteProjectRecords(ArrayList<ProjectRecordUnit> projectRecordUnits) {
    	System.out.println("Main::notifyOverwriteProjectRecords " + projectRecordUnits.size() + " records");
    	summaryFileWriter = SummaryFileWriter.getInstance();
    	if (summaryFileWriter.OverwriteProjectRecords(projectRecordUnits)) {
    		viewerFrame.updateEphrinStatus(projectRecordUnits.size() + " records successfully saved to " + Constants.PROPERTY_SUMMARY_FILE_FULLPATH);
    	} else {
    		viewerFrame.updateEphrinStatus("Alert!! Something went wrong while saving project records!!");
    	}
    }
    
    /**
     * A new txt directory is chosen by the end-user. Read project records and update ViewerFrame
     * @param txtDirectoryName
     */
	public void notifyNewTxtDirectorySelected(String txtDirectoryName) {
		System.out.println("Main::notifyNewTxtDirectorySelected " + txtDirectoryName);
	    TxtDirectoryReader txtDirectoryReader = TxtDirectoryReader.getInstance(); 
	    ArrayList<ProjectRecordUnit> prUnits = txtDirectoryReader.RetrieveProjectRecords(txtDirectoryName);
	    System.out.println(prUnits.size() + " new records found in " + txtDirectoryName);
	    viewerFrame.updateRecordUnits(prUnits);
	}

	/**
	 * Trigger from ViewerFrame to refresh displayed project records. 
	 * The reason behind refreshing is changes to the EphrinSummaryFile.tsv file.  
	 */
	public void notifyRefreshViewerFrame() {
		System.out.println("Main::notifyRefreshViewerFrame");
		viewerFrame.overwriteRecordUnits(retrieveRecordUnits());
	}
}
