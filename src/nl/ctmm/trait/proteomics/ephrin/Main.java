package nl.ctmm.trait.proteomics.ephrin;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;

import nl.ctmm.trait.proteomics.ephrin.gui.SummaryTable;
import nl.ctmm.trait.proteomics.ephrin.gui.ViewerFrame;
import nl.ctmm.trait.proteomics.ephrin.input.TemplateFileReader;
import nl.ctmm.trait.proteomics.ephrin.input.TxtDirectoryReader;
import nl.ctmm.trait.proteomics.ephrin.output.SummaryFileWriter;
import nl.ctmm.trait.proteomics.ephrin.utils.Constants;

public class Main {
	private Properties applicationProperties = null; 
    private ViewerFrame viewerFrame;
    private TemplateFileReader templateFileReader; 
    private SummaryFileWriter summaryFileWriter; 

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
        viewerFrame = new ViewerFrame(appProperties, "Ephrin - Proteomics Project Tracker", instance);
        viewerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        SummaryTable newContentPane = new SummaryTable();
        newContentPane.setOpaque(true); //content panes must be opaque
        viewerFrame.setContentPane(newContentPane);
        //Display the window.
        viewerFrame.pack();
        viewerFrame.setVisible(true);
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

	public void notifyNewTxtDirectorySelected(String txtDirectory) {
		System.out.println("Main::notifyNewTxtDirectorySelected " + txtDirectory);
	    TxtDirectoryReader txtDirectoryReader = new TxtDirectoryReader(txtDirectory); 
	}
}
