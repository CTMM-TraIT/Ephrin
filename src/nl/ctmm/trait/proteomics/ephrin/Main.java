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
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI(Properties appProperties) {
        //Create and set up the window.
        viewerFrame = new ViewerFrame(appProperties, "Ephrin - Proteomics Project Tracker", instance, Constants.SORT_OPTION_NAMES, retrieveRecordUnits());
        viewerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewerFrame.pack();
        viewerFrame.setVisible(true);
        //Create and set up the content pane.
        /*SummaryTable newContentPane = new SummaryTable();
        newContentPane.setOpaque(true); //content panes must be opaque
        viewerFrame.setContentPane(newContentPane);
        //Display the window.
        viewerFrame.pack();
        viewerFrame.setVisible(true);*/
    }

	/**
     * Load the application properties from the properties file.
     *
     * @return the application properties.
     */
    private Properties loadProperties() {
        final Properties appProperties = new Properties();
        // Set default properties.
        //appProperties.setProperty(Constants.PROPERTY_ROOT_FOLDER, Constants.DEFAULT_ROOT_FOLDER);
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

    private ArrayList<ProjectRecordUnit> retrieveRecordUnits() {
    	ArrayList<ProjectRecordUnit> recordUnits = new ArrayList<ProjectRecordUnit>();
    	summaryFileReader = SummaryFileReader.getInstance();
    	recordUnits = summaryFileReader.retrieveProjectRecords();
    	return recordUnits;
    	
    	/*recordUnits.add(new ProjectRecordUnit(1, "QE1_130212_OPL0000", "QE1_130212_OPL0000_jurkat2ug_01|1|L01",
    	         "Z:\\qe-raw-data\\sequences"));
    	recordUnits.add(new ProjectRecordUnit(2, "QE2_130219_OPL0000", "QE2_130219_OPL0000_jurkat2ug_02|1|L04",
    	         "C:\\Xcalibur\\methods\\nanoTune"));
    	recordUnits.add(new ProjectRecordUnit(3, "QE2_130225_OPL0000", "QE2_130225_OPL0000_jurkat2ug_02|1|L25",
    	         "C:\\Xcalibur\\methods\\nanoTune\\ESI_POS_S-lens45"));
    	recordUnits.add(new ProjectRecordUnit(4, "QE2_130415_OPL0000", "QE2_130415_OPL0000_jurkat2ug_01|1|L27",
    	         "C:\\Xcalibur\\methods\\nanoTune\\mstune"));
    	recordUnits.add(new ProjectRecordUnit(5, "QE1_130226_OPL0000", "QE2_130415_OPL0000_jurkat2ug_01|1|L26",
    	         "C:\\qc-data\\QCArchive27Feb\\archive"));*/
    }
    
    public void notifyOverwriteProjectRecords(ArrayList<ProjectRecordUnit> projectRecordUnits) {
    	System.out.println("Main::notifyOverwriteProjectRecords " + projectRecordUnits.size() + " records");
    	summaryFileWriter = SummaryFileWriter.getInstance();
    	if (summaryFileWriter.OverwriteProjectRecords(projectRecordUnits)) {
    		viewerFrame.appendEphrinStatus(projectRecordUnits.size() + " records successfully saved to " + Constants.PROPERTY_SUMMARY_FILE_FULLPATH);
    	} else {
    		viewerFrame.appendEphrinStatus("Alert!! Something went wrong while saving project records!!");
    	}
    }
    
	public void notifyNewTxtDirectorySelected(String txtDirectoryName) {
		System.out.println("Main::notifyNewTxtDirectorySelected " + txtDirectoryName);
	    TxtDirectoryReader txtDirectoryReader = TxtDirectoryReader.getInstance(); 
	    ArrayList<ProjectRecordUnit> prUnits = txtDirectoryReader.RetrieveProjectRecords(txtDirectoryName);
	    System.out.println(prUnits.size() + " new records found in " + txtDirectoryName);
	    viewerFrame.updateRecordUnits(prUnits);
	}
}
