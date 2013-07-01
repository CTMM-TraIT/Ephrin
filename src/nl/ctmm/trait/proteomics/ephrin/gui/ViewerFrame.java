package nl.ctmm.trait.proteomics.ephrin.gui;

/**
 * InternalFrames: http://docs.oracle.com/javase/tutorial/uiswing/components/internalframe.html
 * Radio buttons: http://www.leepoint.net/notes-java/GUI/components/50radio_buttons/25radiobuttons.html
 * 
 * Swing layout: http://www.cs101.org/courses/fall05/resources/swinglayout/
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.ctmm.trait.proteomics.ephrin.Main;
import nl.ctmm.trait.proteomics.ephrin.input.ProjectRecordUnit;
import nl.ctmm.trait.proteomics.ephrin.utils.Constants;
import nl.ctmm.trait.proteomics.ephrin.utils.Utilities;

/**
 * ViewerFrame with the GUI for the QC Report Viewer V2.
 *
 * @author <a href="mailto:pravin.pawar@nbic.nl">Pravin Pawar</a>
 * @author <a href="mailto:freek.de.bruijn@nbic.nl">Freek de Bruijn</a>
 */

public class ViewerFrame extends JFrame implements ActionListener, ItemListener, ChangeListener, MouseListener {
    private static final long serialVersionUID = 1L;
    private JDesktopPane desktopPane = new ScrollDesktop();
    private ArrayList<String> sortOptions = new ArrayList();
    private Main owner = null; 
    private static int RECORD_HEIGHT = 60; 
    private static final int CHECK_PANEL_WIDTH = 140;
    private static final int RECORD_PANEL_WIDTH = 900;
    private static int DESKTOP_PANE_WIDTH = 1070; 
    private int yCoordinate = 0;
    private List<Boolean> recordCheckBoxFlags = new ArrayList<Boolean>();
    private String currentSortCriteria; 
    private List<ProjectRecordUnit> recordUnits = new ArrayList<ProjectRecordUnit>(); //preserve original record units 
    private List<ProjectRecordUnit> orderedRecordUnits = new ArrayList<ProjectRecordUnit>(); //use this list for display and other operations
    private JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT); 
    private static final List<Color> LABEL_COLORS = Arrays.asList(
            Color.BLUE, Color.DARK_GRAY, Color.GRAY, Color.MAGENTA, Color.ORANGE, Color.RED, Color.BLACK);
    
    /**
     * Creates a new instance of the demo.
     * 
     * @param title  the title.
     * @param pipelineStatus 
     */
    public ViewerFrame(final Properties appProperties, final String title, final Main owner, final ArrayList<String> sortOptions, final List<ProjectRecordUnit> recordUnits) {
        super(title);
        System.out.println("ViewerFrame constructor");
        this.owner = owner; 
        this.sortOptions = sortOptions;
        this.recordUnits = recordUnits; 
        this.orderedRecordUnits = recordUnits; //TODO Algorithm for ordering record units
        for (int i = 0; i < recordUnits.size(); ++i) {
        	recordCheckBoxFlags.add(false); //initialize CompareCheckBox flag to false
        }
        for (int i = 0; i < sortOptions.size(); ++i) {
        	System.out.println("sortOption " + i + " = " +sortOptions.get(i));
        }
        setPreferredSize(new Dimension(DESKTOP_PANE_WIDTH, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        assembleComponents();
        //setResizable(false);
        setVisible(true);
        // Finally refresh the frame.
        revalidate();
    }
    
    
    private void assembleComponents() { 
        System.out.println("ViewerFrame assembleComponents");
        //We need one split pane to create 2 regions in the main frame
         //Add static (immovable) Control frame
        JInternalFrame controlFrame = getControlFrame();
        
        int totalRecords = orderedRecordUnits.size();
        
        if (totalRecords != 0) {
        	desktopPane.setPreferredSize(new Dimension(DESKTOP_PANE_WIDTH, totalRecords * (RECORD_HEIGHT + 15)));
            prepareRecordsInAscendingOrder(true);
            splitPane1.add(controlFrame, 0);
            splitPane1.add(new JScrollPane(desktopPane), 1);
            splitPane1.setOneTouchExpandable(true); //hide-show feature
            splitPane1.setDividerLocation(170); //control panel will appear 170 pixels large
            splitPane1.setPreferredSize(new Dimension(DESKTOP_PANE_WIDTH + 15, (int)(6.5 * RECORD_HEIGHT)));
            getContentPane().add(splitPane1, "Center");
            setJMenuBar(createMenuBar());
        }
    }
    
    /*
     *         //Add desktopPane for displaying graphs and other QC Control
        int totalReports = orderedReportUnits.size();
        
        if (totalReports != 0) {
            desktopPane.setPreferredSize(new Dimension(DESKTOP_PANE_WIDTH, totalReports * (CHART_HEIGHT + 15)));
            prepareChartsInAscendingOrder(true);
            splitPane2.add(new JScrollPane(desktopPane), 0);
            ticGraphPane.setPreferredSize(new Dimension(DESKTOP_PANE_WIDTH, 2 * CHART_HEIGHT));
            splitPane2.add(new JScrollPane(ticGraphPane), 1);
            //Set initial tic Graph - specify complete chart in terms of orderedReportUnits
            setTicGraphPaneChart(orderedReportUnits.get(0).getReportNum() - 1);
            splitPane2.setOneTouchExpandable(true); //hide-show feature
            splitPane2.setDividerLocation(500); //DesktopPane holding graphs will appear 500 pixels large
            splitPane1.add(controlFrame);
            splitPane1.add(splitPane2);
            splitPane1.setOneTouchExpandable(true); //hide-show feature
            splitPane1.setDividerLocation(170); //control panel will appear 170 pixels large
            splitPane1.setPreferredSize(new Dimension(DESKTOP_PANE_WIDTH + 15, (int)(6.5 * CHART_HEIGHT)));
            getContentPane().add(splitPane1, "Center");
            setJMenuBar(createMenuBar());
        } 
     */
    
    
    /**
     * Create Menu Bar for settings and about tab
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu settingsMenu = new JMenu("Operations");
        menuBar.add(settingsMenu);
        JMenuItem newDirAction = new JMenuItem("Search Directory...");
        settingsMenu.add(newDirAction);
        newDirAction.setActionCommand("SearchDirectory");
        newDirAction.addActionListener(this);
        JMenuItem aboutAction = new JMenuItem("About...");
        settingsMenu.add(aboutAction);
        aboutAction.setActionCommand("About");
        aboutAction.addActionListener(this);        
        return menuBar;
    }
    
    /**
     * Prepare and return controlFrame
     * @return JInternalFrame controlFrame
     */
    private JInternalFrame getControlFrame() {
        System.out.println("ViewerFrame getControlFrame");
        JInternalFrame controlFrame = new JInternalFrame("Control Panel", true);
        javax.swing.plaf.InternalFrameUI ifu= controlFrame.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI)ifu).setNorthPane(null);
        controlFrame.setBorder(null);
        controlFrame.setLayout(new BorderLayout(0, 0));
        controlFrame.setBackground(Color.WHITE);
        
        ButtonGroup sortGroup = new ButtonGroup();

        GridLayout layout = new GridLayout(sortOptions.size()/2+1,2);
        ArrayList<JRadioButton> sortButtons = new ArrayList<JRadioButton>();
        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(layout);
        sortPanel.setPreferredSize(new Dimension(700, 130));
        sortPanel.setBackground(Color.WHITE); 
        int style = Font.BOLD;
        Font font = new Font ("Garamond", style , 11);
        for (int i = 0; i < sortOptions.size(); ++i) {
            JLabel thisLabel = new JLabel(sortOptions.get(i) + ": ");
            thisLabel.setFont(font);
            thisLabel.setBackground(Color.WHITE);
            JPanel namePanel = new JPanel(new GridLayout(1,1));
            namePanel.add(thisLabel);
            namePanel.setBackground(Color.WHITE);
            //Sort ascending button
            JRadioButton ascButton = new JRadioButton("Asc", false);
            ascButton.setBackground(Color.WHITE);
            ascButton.setActionCommand("Sort@" + sortOptions.get(i) + "@Asc");
            ascButton.addActionListener(this);
            sortGroup.add(ascButton);
            sortButtons.add(ascButton);
            //Sort descending button
            JRadioButton desButton = new JRadioButton("Des", false);
            desButton.setBackground(Color.WHITE);
            desButton.setActionCommand("Sort@" + sortOptions.get(i) + "@Des");
            desButton.addActionListener(this);
            sortGroup.add(desButton); 
            sortButtons.add(desButton);
            JPanel buttonPanel = new JPanel(new GridLayout(1,2));
            buttonPanel.add(ascButton);
            buttonPanel.add(desButton);
            JPanel metricPanel = new JPanel(new GridLayout(1,2));
            metricPanel.add(namePanel, 0);
            metricPanel.add(buttonPanel, 1);
            sortPanel.add(metricPanel);
        }
        //Add sorting according to Compare 
        JLabel thisLabel = new JLabel("Compare: ");
        thisLabel.setFont(font);
        thisLabel.setBackground(Color.WHITE);
        JPanel namePanel = new JPanel(new GridLayout(1,1));
        namePanel.setBackground(Color.WHITE);
        namePanel.add(thisLabel);
        //Sort ascending button
        JRadioButton ascButton = new JRadioButton("Asc", false);
        ascButton.setBackground(Color.WHITE);
        ascButton.setActionCommand("Sort@" + "Compare" + "@Asc");
        ascButton.addActionListener(this);
        sortGroup.add(ascButton);
        sortButtons.add(ascButton);
        //Sort descending button
        JRadioButton desButton = new JRadioButton("Des", false);
        desButton.setBackground(Color.WHITE);
        desButton.setActionCommand("Sort@" + "Compare" + "@Des");
        desButton.addActionListener(this);
        sortGroup.add(desButton); 
        sortButtons.add(desButton); 
        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        buttonPanel.add(ascButton);
        buttonPanel.add(desButton);
        JPanel metricPanel = new JPanel(new GridLayout(1,2));
        metricPanel.add(namePanel, 0);
        metricPanel.add(buttonPanel, 1);
        sortPanel.add(metricPanel);
        
        //Set first button selected
        sortButtons.get(0).setSelected(true);
        this.currentSortCriteria = sortButtons.get(0).getActionCommand(); 
        sortPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Sort Options"));

        //Add ephrinLogo to control frame
        BufferedImage ephrinLogo = null;
        try {
        	BufferedImage bigEphrinLogo = ImageIO.read(new File(Constants.PROPERTY_EPHRIN_LOGO_FILE));
        	ephrinLogo = Utilities.scaleImage(bigEphrinLogo, Utilities.SCALE_FIT, 150, 122);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel oplLabel = new JLabel(new ImageIcon(ephrinLogo));
        JPanel oplPanel = new JPanel();
        oplPanel.add(oplLabel);
        
        //Add traitlogo to control frame
        BufferedImage traitctmmLogo = null;
        try {
            traitctmmLogo = ImageIO.read(new File(Constants.PROPERTY_PROJECT_LOGO_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel traitctmmLabel = new JLabel(new ImageIcon(traitctmmLogo));
        JPanel traitctmmPanel = new JPanel();
        traitctmmPanel.add(traitctmmLabel);
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setPreferredSize(new Dimension(800, 150));
        controlPanel.add(oplPanel, 0);
        controlPanel.add(sortPanel, 1);
        controlPanel.add(traitctmmPanel, 2);
        
        controlFrame.getContentPane().add(controlPanel, BorderLayout.NORTH);
        String status = "Number of project record units = " + orderedRecordUnits.size(); 
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(font);
        statusLabel.setBackground(Color.CYAN);
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(Color.CYAN); 
        statusPanel.add(statusLabel);
        controlFrame.getContentPane().add(statusPanel, BorderLayout.SOUTH);
        controlFrame.setSize(new Dimension(DESKTOP_PANE_WIDTH + 30, 170));
        controlFrame.pack();
        controlFrame.setLocation(0, 0);
        controlFrame.setResizable(false); 

        //TODO avoid resizing and repositioning of components in the controlFrame
        controlPanel.addComponentListener(new ComponentListener() {  
            public void componentResized(ComponentEvent e) {  
                //JPanel controlPanel = (JPanel)e.getSource();  
                //controlPanel.setSize(new Dimension(DESKTOP_PANE_WIDTH + 30, 170));
            }
            @Override
            public void componentHidden(ComponentEvent arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void componentMoved(ComponentEvent arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void componentShown(ComponentEvent arg0) {
                // TODO Auto-generated method stub
            }  
        });  
        controlFrame.setVisible(true);
        return controlFrame;
    }
    
    /**
     * Creates an internal frame.
     * setSelected is required to preserve check boxes status in the display
     * @return An internal frame.
     */
    private JInternalFrame createRecordFrame(int displayNum, ProjectRecordUnit recordUnit) {
        System.out.print("ViewerFrame createRecordFrame " + recordUnit.getRecordNum() + " ");
        //Create the visible record panel
        final JInternalFrame frame = new JInternalFrame("Record " + displayNum, true);
        frame.setName(Integer.toString(recordUnit.getRecordNum() - 1)); //Set report index number as frame name
        javax.swing.plaf.InternalFrameUI ifu= frame.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI)ifu).setNorthPane(null);
        int style = Font.BOLD;
        Font font = new Font ("Garamond", style , 11);
        //Create a checkbox for selection
        JCheckBox recordCheckBox = new JCheckBox("Compare");
        recordCheckBox.setFont(font);
        recordCheckBox.setBackground(Color.WHITE);
      //recordCheckBoxFlags are organized according to original record num 
        recordCheckBox.setName(Integer.toString(recordUnit.getRecordNum() - 1)); //Since reportNum is > 0
        if (recordCheckBoxFlags.get(recordUnit.getRecordNum() - 1)) {
        	recordCheckBox.setSelected(true); 
        } else recordCheckBox.setSelected(false);
        recordCheckBox.addItemListener(this);
        JButton transmartButton = new JButton("TranSMART");
        transmartButton.setFont(font);
        transmartButton.setPreferredSize(new Dimension(100, 20));
        transmartButton.setActionCommand("TranSMART-" + Integer.toString(recordUnit.getRecordNum()));
        transmartButton.addActionListener(this);
        
        JPanel checkPanel = new JPanel();
        checkPanel.setFont(font);
        checkPanel.setBackground(Color.WHITE);
        checkPanel.setForeground(Color.WHITE); 
        //GridLayout layout = new GridLayout(2, 1);
        //checkPanel.setLayout(layout);
        checkPanel.add(transmartButton, 0);
        checkPanel.add(recordCheckBox, 1);
        JLabel numLabel = new JLabel(Integer.toString(recordUnit.getRecordNum()));
        Font numFont = new Font ("Garamond", style , 22);
        numLabel.setFont(numFont);
        checkPanel.add(numLabel);
        checkPanel.setPreferredSize(new Dimension(CHECK_PANEL_WIDTH, RECORD_HEIGHT));
        
        JPanel recordPanel = new JPanel();
        recordPanel.setFont(font);
        recordPanel.setBackground(Color.WHITE);
        GridLayout layout = new GridLayout(1, sortOptions.size());
        recordPanel.setLayout(layout);
        recordPanel.setPreferredSize(new Dimension(RECORD_PANEL_WIDTH, RECORD_HEIGHT));
        // add record labels, one in each cell
        Color fgColor = LABEL_COLORS.get(displayNum%LABEL_COLORS.size());
        JLabel thisLabel = new JLabel(recordUnit.getProjectName());
        thisLabel.setFont(font);
        thisLabel.setForeground(fgColor);
        recordPanel.add(thisLabel);
        thisLabel = new JLabel(recordUnit.getFirstRawFile());
        thisLabel.setFont(font);
        thisLabel.setForeground(fgColor);
        recordPanel.add(thisLabel);
        thisLabel = new JLabel(recordUnit.getFolderPath());
        thisLabel.setFont(font);
        thisLabel.setForeground(fgColor);
        recordPanel.add(thisLabel);
        JPanel displayPanel = new JPanel();
        displayPanel.add(checkPanel, 0);
        displayPanel.add(recordPanel, 1);
        displayPanel.setBorder(null);
        frame.getContentPane().add(displayPanel);
        frame.addMouseListener(this);
        frame.setBorder(null);
        return frame;
    }

    /**
     * 
     * @param flag if true, records will be prepared in ascending order. if false, the records will be prepared in descending order
     */
    private void prepareRecordsInAscendingOrder(boolean flag) {
        System.out.println("ViewerFrame prepareRecordsInAscendingOrder");
        yCoordinate = 0;
        System.out.println("No. of orderedReportUnits = " + orderedRecordUnits.size());
        for (int i = 0; i < orderedRecordUnits.size(); ++i) {
            JInternalFrame recordFrame;
            if (flag) {
            	recordFrame = createRecordFrame(i, orderedRecordUnits.get(i));
            } else {
                int index = orderedRecordUnits.size() - i - 1;
                recordFrame = createRecordFrame(i, orderedRecordUnits.get(orderedRecordUnits.size() - i - 1));
            }
            recordFrame.setBorder(BorderFactory.createRaisedBevelBorder());
            recordFrame.pack();
            recordFrame.setLocation(0, yCoordinate);
            recordFrame.setVisible(true);
            desktopPane.add(recordFrame);
            System.out.println("yCoordinate = " + yCoordinate);
            yCoordinate += RECORD_HEIGHT + 15;
        }
    }
    
    /**
     * Process user input events.
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Corresponding action command is " + evt.getActionCommand() 
                + " evt class = " + evt.getClass());
        //Check whether Details button is pressed - in order to open corresponding hyperlink 

        if (evt.getActionCommand().equals("SearchDirectory")) {
        	String txtDirectory = displayTxtDirectoryChooser();
        	if (txtDirectory != null && owner != null) {
        		owner.notifyNewTxtDirectorySelected(txtDirectory);
        	}
        }
    }
    
    /**
     * Remove and nullify all the report units GUI components
     */
    public void clean() {
    }
    
    @Override
    public void mouseClicked(MouseEvent arg0) {
    } 

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }
    
	
	 public String displayTxtDirectoryChooser () {
	 JFileChooser chooser = new JFileChooser();
	 	chooser.setName("Select txt Folder");
		chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
	    int returnVal = chooser.showOpenDialog(null);
	    String txtDirectory = null;
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	txtDirectory = chooser.getSelectedFile().getAbsolutePath();
	       System.out.println("Selected folder: " +
	            chooser.getSelectedFile().getAbsolutePath());
		    dispose();
	    } 
	    return txtDirectory;
	 }


	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}

}

