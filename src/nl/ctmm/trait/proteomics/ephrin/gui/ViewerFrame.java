package nl.ctmm.trait.proteomics.ephrin.gui;

/**
 * InternalFrames: http://docs.oracle.com/javase/tutorial/uiswing/components/internalframe.html
 * Radio buttons: http://www.leepoint.net/notes-java/GUI/components/50radio_buttons/25radiobuttons.html
 * 
 * Swing layout: http://www.cs101.org/courses/fall05/resources/swinglayout/
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
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
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import nl.ctmm.trait.proteomics.ephrin.Main;
import nl.ctmm.trait.proteomics.ephrin.input.ProjectRecordUnit;
import nl.ctmm.trait.proteomics.ephrin.input.SummaryFileReader;
import nl.ctmm.trait.proteomics.ephrin.utils.Constants;
import nl.ctmm.trait.proteomics.ephrin.utils.Utilities;


/**
 * ViewerFrame with the GUI for the Ephrin project
 *
 * @author <a href="mailto:pravin.pawar@nbic.nl">Pravin Pawar</a>
 * @author <a href="mailto:freek.de.bruijn@nbic.nl">Freek de Bruijn</a>
 */

//TODO Algorithm for ordering record units
//TODO Code refactoring
//TODO Red status update
//TODO Check for duplicate records
//TODO SplitPane1 position 

public class ViewerFrame extends JFrame implements ActionListener, ItemListener, FocusListener {
    private static final long serialVersionUID = 1L;
    private JDesktopPane desktopPane = new ScrollDesktop();
    private ArrayList<String> sortOptions = new ArrayList<String>();
    private String[] categoryStrings;
    private Main owner = null; 
    private static int RECORD_HEIGHT = 60; 
    private static final int CHECK_PANEL_WIDTH = 120;
    private static final int RECORD_PANEL_WIDTH = 800;
    private static final int CATEGORY_PANEL_WIDTH = 100;
    private static final int COMMENT_PANEL_WIDTH = 140;
    private static final int SPLIT_PANE1_DIVIDER_LOCATION = 190;
    private static final int SORT_PANEL_WIDTH = 600;
    private static final int SORT_PANEL_HEIGHT = 130; 
    private static final int EPHRIN_LOGO_HEIGHT = 130; 
    private static final int EPHRIN_LOGO_WIDTH = 122;
    private static final int CONTROL_PANEL_WIDTH = 1230; //includes oplPanel, sortPanel, traitctmmPanel
    private static final int CONTROL_PANEL_HEIGHT = 150;
    private static final int STATUS_PANEL_WIDTH = 1230;
    private static final int STATUS_PANEL_HEIGHT = 25;
    private static final int CONTROL_FRAME_WIDTH = 1000; //includes oplPanel, sortPanel, traitctmmPanel
    private static final int CONTROL_FRAME_HEIGHT = 180;
    private static final int DESKTOP_PANE_WIDTH = 1230; //Same as ViewerFrameWidth
    private static final int VIEWER_FRAME_HEIGHT = 1000; 
    public static final int TRANSMART_BUTTON_WIDTH = 100; 
    public static final int TRANSMART_BUTTON_HEIGHT = 20;
    
    private Font statusFont = new Font ("Garamond", Font.BOLD , 11);
    private int yCoordinate = 0;
    private  JInternalFrame controlFrame; 
    //Check boxes to keep track of check boxes
    private List<Boolean> recordCheckBoxFlags = new ArrayList<Boolean>();
    //Category List to keep track of edited categories
    private List<String> recordCategories = new ArrayList<String>();
    //Comment List to keep track of edited comments
    private List<String> recordComments = new ArrayList<String>();
    private String currentSortCriteria; 
    //private ArrayList<ProjectRecordUnit> recordUnits = new ArrayList<ProjectRecordUnit>(); //preserve original record units 
    private ArrayList<ProjectRecordUnit> orderedRecordUnits = new ArrayList<ProjectRecordUnit>(); //use this list for display and other operations
    private JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT); 
    private static final List<Color> LABEL_COLORS = Arrays.asList(Color.BLUE, Color.DARK_GRAY);
    //, Color.GRAY, Color.MAGENTA, Color.ORANGE, Color.RED, Color.BLACK
    private JPanel statusPanel;
    private JLabel statusLabel;
    private String currentStatus = ""; 
    
    /**
     * Constructor of the ViewerFrame
     * @param appProperties Application properties
     * @param title Title of the ViewerFrame
     * @param owner instance of the Main class
     * @param sortOptions Options for sorting project records
     * @param categories for Combo box
     * @param recordUnits Project record units to be displayed
     */
    public ViewerFrame(final String title, final Main owner, final ArrayList<String> sortOptions, final ArrayList<String> categories, final ArrayList<ProjectRecordUnit> recordUnits) {
        super(title);
        System.out.println("ViewerFrame constructor");
        this.owner = owner; 
        this.sortOptions = sortOptions;
        //Convert Categories ArrayList to String[] format for input to JComboBox
        categoryStrings = new String[categories.size()]; 
        for (int i = 0; i < categories.size(); ++i) { 
        	categoryStrings[i] = categories.get(i);
        }
        overwriteRecordUnits(recordUnits);
    }
    
    /**
     * Overwrite all project record units
     * The reason behind overwriting is changes to the EphrinSummaryFile.tsv file.
     * @param newRecordUnits
     */
    public void overwriteRecordUnits(List<ProjectRecordUnit> newRecordUnits) {
    	getContentPane().removeAll();
    	cleanVariables();
        for (int i = 0; i < newRecordUnits.size(); ++i) {
        	orderedRecordUnits.add(newRecordUnits.get(i));
        	//initialize MarkCheckBox flag to false, linked to orderedRecordUnits
        	recordCheckBoxFlags.add(false); 
        	//initialize recordCategory, linked to orderedRecordUnits
        	//It is assumed that category is 3rd index in sortOptions
        	recordCategories.add(newRecordUnits.get(i).getParameterValueFromKey(sortOptions.get(3))); 
        	//initialize recordComment, linked to orderedRecordUnits
        	//It is assumed that category is 3rd index in sortOptions
        	recordComments.add(newRecordUnits.get(i).getParameterValueFromKey(sortOptions.get(4)));
        }
        setPreferredSize(new Dimension(DESKTOP_PANE_WIDTH, VIEWER_FRAME_HEIGHT));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        assembleComponents();
        setResizable(false);
        pack();
        setVisible(true);
        revalidate();
    }

    /**
     * Assemble following components of the ViewerFrame:
     * 1) ControlPanel 2) DesktopPane 
     */
    private void assembleComponents() { 
        System.out.println("ViewerFrame assembleComponents");
        //We need one split pane to create 2 regions in the main frame
        //Add static (immovable) Control frame
        cleanGUIComponents();
        int totalRecords = orderedRecordUnits.size();
        if (totalRecords != 0) {
        	desktopPane.setPreferredSize(new Dimension(DESKTOP_PANE_WIDTH, (totalRecords + 1) * (RECORD_HEIGHT + 5)));
            prepareRecordsInAscendingOrder(true);
            assembleSplitPane1();
            setJMenuBar(createMenuBar());
        }
    }
    
    /**
     * Creates a splitPane1 seperating controlFrame and desktopPane 
     * TODO Solve problems regarding divider location, display and 
     * redrawing splitPane on adding new record or deleting marked record or saving record
     */
    private void assembleSplitPane1() {
    	splitPane1.removeAll();
    	controlFrame = getControlFrame();
    	controlFrame.setVisible(true);
        splitPane1.setVisible(true); 
        splitPane1.add(controlFrame, 0);
        JScrollPane recordsPane = new JScrollPane(desktopPane);
        recordsPane.setVisible(true);
        int totalRecords = orderedRecordUnits.size();
        recordsPane.setMinimumSize(new Dimension(DESKTOP_PANE_WIDTH, (totalRecords + 1) * (RECORD_HEIGHT + 5)));
        splitPane1.add(recordsPane, 1);
        splitPane1.setDividerLocation(SPLIT_PANE1_DIVIDER_LOCATION);
        splitPane1.setOneTouchExpandable(true); //hide-show feature
        getContentPane().add(splitPane1, "Center");
    }
    
    /**
     * Create Menubar with Operations option
     * @return Menubar to be displayed in the ViewerFrame
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu settingsMenu = new JMenu("Operations");
        menuBar.add(settingsMenu);
        //Import project records from MaxQuant directory
        JMenuItem newDirAction = new JMenuItem("Import Directory...");
        settingsMenu.add(newDirAction);
        newDirAction.setActionCommand("ImportDirectory");
        newDirAction.addActionListener(this);
        //Save all records in desktopPane
        JMenuItem saveRecordsAction = new JMenuItem("Save All Records...");
        settingsMenu.add(saveRecordsAction);
        saveRecordsAction.setActionCommand("SaveAllRecords");
        saveRecordsAction.addActionListener(this); 
        //Delete marked records
        JMenuItem deleteRecordsAction = new JMenuItem("Delete Marked Records...");
        settingsMenu.add(deleteRecordsAction);
        deleteRecordsAction.setActionCommand("DeleteMarkedRecords");
        deleteRecordsAction.addActionListener(this);
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
        JInternalFrame controlFrame = new JInternalFrame("Control Panel", false);
        javax.swing.plaf.InternalFrameUI ifu= controlFrame.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI)ifu).setNorthPane(null);
        controlFrame.setBorder(null);
        controlFrame.setPreferredSize(new Dimension(CONTROL_FRAME_WIDTH, CONTROL_FRAME_HEIGHT));
        controlFrame.setMinimumSize(new Dimension(CONTROL_FRAME_WIDTH, CONTROL_FRAME_HEIGHT));
        controlFrame.setLayout(new FlowLayout());
        controlFrame.setBackground(Color.WHITE);
        
        ButtonGroup sortGroup = new ButtonGroup();
        GridLayout layout = new GridLayout(sortOptions.size()/2+1,2);
        ArrayList<JRadioButton> sortButtons = new ArrayList<JRadioButton>();
        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(layout);
        sortPanel.setPreferredSize(new Dimension(SORT_PANEL_WIDTH, SORT_PANEL_HEIGHT));
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
            buttonPanel.setBackground(Color.WHITE); 
            JPanel metricPanel = new JPanel(new GridLayout(1,2));
            metricPanel.add(namePanel, 0);
            metricPanel.add(buttonPanel, 1);
            metricPanel.setBackground(Color.WHITE); 
            sortPanel.add(metricPanel);
        }
        //Add sorting according to Mark 
        JLabel thisLabel = new JLabel("Mark: ");
        thisLabel.setFont(font);
        thisLabel.setBackground(Color.WHITE);
        JPanel namePanel = new JPanel(new GridLayout(1,1));
        namePanel.setBackground(Color.WHITE);
        namePanel.add(thisLabel);
        //Sort ascending button
        JRadioButton ascButton = new JRadioButton("Asc", false);
        ascButton.setBackground(Color.WHITE);
        ascButton.setActionCommand("Sort@" + "Mark" + "@Asc");
        ascButton.addActionListener(this);
        sortGroup.add(ascButton);
        sortButtons.add(ascButton);
        //Sort descending button
        JRadioButton desButton = new JRadioButton("Des", false);
        desButton.setBackground(Color.WHITE);
        desButton.setActionCommand("Sort@" + "Mark" + "@Des");
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
        	ephrinLogo = Utilities.scaleImage(bigEphrinLogo, Utilities.SCALE_FIT, EPHRIN_LOGO_WIDTH, EPHRIN_LOGO_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel oplLabel = new JLabel(new ImageIcon(ephrinLogo));
        JPanel oplPanel = new JPanel();
        oplPanel.add(oplLabel);
        oplPanel.setBackground(Color.WHITE);
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
        traitctmmPanel.setBackground(Color.WHITE);
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH, CONTROL_PANEL_HEIGHT));
        controlPanel.add(oplPanel, 0);
        controlPanel.add(sortPanel, 1);
        controlPanel.add(traitctmmPanel, 2);
        //controlPanel.setBackground(Color.WHITE);
        controlFrame.getContentPane().add(controlPanel);
        if (currentStatus.equals("")) {
            currentStatus = "Number of project record units = " + orderedRecordUnits.size(); 
        }
        statusLabel = new JLabel(currentStatus);
        statusLabel.setFont(statusFont);
        statusLabel.setBackground(Color.CYAN);
        statusPanel = new JPanel();
        statusPanel.setBackground(Color.CYAN); 
        statusPanel.add(statusLabel);
        statusPanel.setPreferredSize(new Dimension(STATUS_PANEL_WIDTH, STATUS_PANEL_HEIGHT)); 
        controlFrame.getContentPane().add(statusPanel);
        controlFrame.pack();
        controlFrame.setLocation(0, 0);
        controlFrame.setResizable(false); 
        controlFrame.setVisible(true);
        return controlFrame;
    }

    /**
     * Create Internal Frame holding one project record unit
     * @param displayNum Number of record unit
     * @param recordUnit Record unit to be displayed
     * @return
     */
    private JInternalFrame createRecordFrame(int displayNum, ProjectRecordUnit recordUnit) {
        System.out.print("ViewerFrame createRecordFrame " + recordUnit.getRecordNum() + " ");
        //Create the visible record panel
        final JInternalFrame frame = new JInternalFrame("Record " + displayNum, true);
        frame.setName(Integer.toString(recordUnit.getRecordNum() - 1)); //Set report index number as frame name
        javax.swing.plaf.InternalFrameUI ifu= frame.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI)ifu).setNorthPane(null);
        int style = Font.PLAIN;
        Font font = new Font ("Garamond", style , 11);
        //Create a checkbox for selection
        JCheckBox recordCheckBox = new JCheckBox("Mark");
        recordCheckBox.setFont(font);
        recordCheckBox.setBackground(Color.WHITE);
      //recordCheckBoxFlags are organized according to original record num 
        recordCheckBox.setName(Integer.toString(recordUnit.getRecordNum() - 1)); //Since reportNum is > 0
        if (recordCheckBoxFlags.get(recordUnit.getRecordNum() - 1)) {
        	recordCheckBox.setSelected(true); 
        } else recordCheckBox.setSelected(false);
        recordCheckBox.addItemListener(this);
        JPanel checkPanel = new JPanel();
        checkPanel.setFont(font);
        checkPanel.setBackground(Color.WHITE);
        checkPanel.setForeground(Color.WHITE); 
        JLabel numLabel = new JLabel(Integer.toString(recordUnit.getRecordNum()));
        Font numFont = new Font ("Garamond", style , 22);
        numLabel.setFont(numFont);

        JButton transmartButton = new JButton("tranSMART");
        transmartButton.setFont(font);
        transmartButton.setPreferredSize(new Dimension(100, 20));
        transmartButton.setActionCommand("tranSMART-" + Integer.toString(recordUnit.getRecordNum()));
        transmartButton.addActionListener(this);

        checkPanel.add(transmartButton, 0);
        checkPanel.add(numLabel, 1);
        checkPanel.add(recordCheckBox, 2);
        
        checkPanel.setPreferredSize(new Dimension(CHECK_PANEL_WIDTH, RECORD_HEIGHT));
        
        JPanel recordPanel = new JPanel();
        recordPanel.setFont(font);
        recordPanel.setBackground(Color.WHITE);
        GridLayout layout = new GridLayout(1, 3); //3 labels for ProjectName, FirstRawFile and ProjectFolder
        recordPanel.setLayout(layout);
        recordPanel.setPreferredSize(new Dimension(RECORD_PANEL_WIDTH, RECORD_HEIGHT));
        // add record labels, one in each cell
        Color fgColor = LABEL_COLORS.get(displayNum%LABEL_COLORS.size());
        
        JTextArea recordArea = new JTextArea();
        recordArea.setText(recordUnit.getParameterValueFromKey(sortOptions.get(0)));
        recordArea.setEditable(false);
        recordArea.setFont(font);
        recordArea.setLineWrap(true);
        recordArea.setForeground(fgColor);
        recordArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        recordPanel.add(recordArea);
        
        recordArea = new JTextArea();
        recordArea.setText(recordUnit.getParameterValueFromKey(sortOptions.get(1)));
        recordArea.setEditable(false);
        recordArea.setFont(font);
        recordArea.setLineWrap(true);
        recordArea.setForeground(fgColor);
        recordArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        recordPanel.add(recordArea);
        
        recordArea = new JTextArea();
        recordArea.setText(recordUnit.getParameterValueFromKey(sortOptions.get(2)));
        recordArea.setEditable(false);
        recordArea.setFont(font);
        recordArea.setLineWrap(true);
        recordArea.setForeground(fgColor);
        recordArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        recordPanel.add(recordArea);
        
        //Create the combo box, select item at index 2.
        //Indices start at 0, so 2 specifies the unknown.

        JComboBox<String> categoryCombo = new JComboBox<String>(categoryStrings);
        categoryCombo.setFont(font);
        categoryCombo.setName(Integer.toString(recordUnit.getRecordNum() - 1));
        categoryCombo.setSelectedItem(recordUnit.getParameterValueFromKey(sortOptions.get(3)));
        categoryCombo.addActionListener(this);
        JPanel categoryPanel = new JPanel();
        categoryPanel.setFont(font);
        categoryPanel.setBackground(Color.WHITE);
        categoryPanel.setPreferredSize(new Dimension(CATEGORY_PANEL_WIDTH, RECORD_HEIGHT));
        categoryPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        categoryPanel.add(categoryCombo);
        
      //Add JTextArea for entering comment
        JTextArea commentArea = new JTextArea();
        commentArea.setBorder(null);
        commentArea.setName(Integer.toString(recordUnit.getRecordNum() - 1));
        commentArea.setText(recordUnit.getParameterValueFromKey(sortOptions.get(4)));
        commentArea.setPreferredSize(new Dimension(COMMENT_PANEL_WIDTH - 3, RECORD_HEIGHT - 5));
        commentArea.setEditable(true);
        commentArea.setLineWrap(true); 

        commentArea.addFocusListener(this);
        JPanel commentPanel = new JPanel();
        commentPanel.setFont(font);
        commentPanel.setBackground(Color.WHITE);
        commentPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        commentPanel.setPreferredSize(new Dimension(COMMENT_PANEL_WIDTH, RECORD_HEIGHT));
        commentPanel.add(commentArea);
        
        JPanel displayPanel = new JPanel();
        displayPanel.add(checkPanel, 0);
        displayPanel.add(recordPanel, 1);
        displayPanel.add(categoryPanel, 2);
        displayPanel.add(commentPanel, 3);
        displayPanel.setBorder(null);
        frame.getContentPane().add(displayPanel);
        frame.setBorder(null);
        return frame;
    }

    /**
     * Create Internal Frame holding Titles of columns
     * @return
     */
    private JInternalFrame createTitleFrame() {
        System.out.print("ViewerFrame createTitleFrame");
        //Create the visible record panel
        final JInternalFrame frame = new JInternalFrame("TitleFrame", false, false);
        frame.setName("TitleFrame");
        javax.swing.plaf.InternalFrameUI ifu= frame.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI)ifu).setNorthPane(null);
        int style = Font.BOLD;
        Font font = new Font ("Garamond", style , 12);
        JLabel checkLabel = new JLabel("CheckPanel");
        checkLabel.setFont(font);
        checkLabel.setBackground(Color.WHITE);
        JPanel checkPanel = new JPanel();
        checkPanel.setFont(font);
        checkPanel.setBackground(Color.WHITE);
        checkPanel.setForeground(Color.WHITE); 
        checkPanel.add(checkLabel);
        checkPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        checkPanel.setPreferredSize(new Dimension(CHECK_PANEL_WIDTH, RECORD_HEIGHT/2));
        JPanel recordPanel = new JPanel();
        recordPanel.setFont(font);
        recordPanel.setBackground(Color.WHITE);
        GridLayout layout = new GridLayout(1, 3); //3 labels for ProjectName, FirstRawFile and ProjectFolder
        recordPanel.setLayout(layout);
        recordPanel.setPreferredSize(new Dimension(RECORD_PANEL_WIDTH, RECORD_HEIGHT/2));
        // add record labels, one in each cell
        JLabel titleLabel = new JLabel(sortOptions.get(0));
        titleLabel.setFont(font);
        titleLabel.setHorizontalAlignment( SwingConstants.CENTER);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(RECORD_PANEL_WIDTH/3, RECORD_HEIGHT/2));
        titleLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        recordPanel.add(titleLabel);
        titleLabel = new JLabel(sortOptions.get(1));
        titleLabel.setFont(font);
        titleLabel.setHorizontalAlignment( SwingConstants.CENTER);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(RECORD_PANEL_WIDTH/3, RECORD_HEIGHT/2));
        titleLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        recordPanel.add(titleLabel);
        titleLabel = new JLabel(sortOptions.get(2));
        titleLabel.setFont(font);
        titleLabel.setHorizontalAlignment( SwingConstants.CENTER);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        recordPanel.add(titleLabel);

        //Add the combo box label
        JLabel categoryLabel = new JLabel(sortOptions.get(3));
        categoryLabel.setFont(font);
        categoryLabel.setBackground(Color.WHITE);
        JPanel categoryPanel = new JPanel();
        categoryPanel.setFont(font);
        categoryPanel.setBackground(Color.WHITE);
        categoryPanel.setPreferredSize(new Dimension(CATEGORY_PANEL_WIDTH, RECORD_HEIGHT/2));
        categoryPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        categoryPanel.add(categoryLabel);
        
      //Add the comment box label
        JLabel commentLabel = new JLabel(sortOptions.get(4));
        commentLabel.setFont(font);
        commentLabel.setBackground(Color.WHITE);
        JPanel commentPanel = new JPanel();
        commentPanel.setFont(font);
        commentPanel.setBackground(Color.WHITE);
        commentPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        commentPanel.setPreferredSize(new Dimension(COMMENT_PANEL_WIDTH, RECORD_HEIGHT/2));
        commentPanel.add(commentLabel);
        
        JPanel displayPanel = new JPanel();
        displayPanel.add(checkPanel, 0);
        displayPanel.add(recordPanel, 1);
        displayPanel.add(categoryPanel, 2);
        displayPanel.add(commentPanel, 3);
        displayPanel.setBorder(null);
        frame.getContentPane().add(displayPanel);
        frame.setBorder(null);
        return frame;
    }
    
    /**
     * Prepare records to be displayed
     * @param flag if true, records will be prepared in ascending order. if false, the records will be prepared in descending order
     */
    private void prepareRecordsInAscendingOrder(boolean flag) {
        System.out.println("ViewerFrame prepareRecordsInAscendingOrder");
        yCoordinate = 5;
        JInternalFrame titleFrame = createTitleFrame();
        titleFrame.pack();
        titleFrame.setLocation(0, yCoordinate);
        titleFrame.setVisible(true);
        desktopPane.add(titleFrame);
        yCoordinate += RECORD_HEIGHT/2 + 5;
        System.out.println("No. of orderedRecordUnits = " + orderedRecordUnits.size());
        for (int i = 0; i < orderedRecordUnits.size(); ++i) {
            JInternalFrame recordFrame;
            if (flag) {
            	recordFrame = createRecordFrame(i, orderedRecordUnits.get(i));
            } else {
                recordFrame = createRecordFrame(i, orderedRecordUnits.get(orderedRecordUnits.size() - i - 1));
            }
            recordFrame.pack();
            recordFrame.setLocation(0, yCoordinate);
            recordFrame.setVisible(true);
            desktopPane.add(recordFrame);
            System.out.println("yCoordinate = " + yCoordinate);
            yCoordinate += RECORD_HEIGHT + 5;
        }
    }
    
    /**
     * Add new project record units to existing list
     * Display new project record units
     * @param newRecordUnits
     */
    public void addRecordUnit(ProjectRecordUnit newRecordUnit) {
        System.out.println("In updateRecordUnits yCoordinate = " + yCoordinate);
        int numRecordUnits = orderedRecordUnits.size();
        System.out.println("Number of project record units = " + orderedRecordUnits.size());
        String appendMessage = "";
        if (!duplicateRecordsCheck(newRecordUnit)) {
        	newRecordUnit.setRecordNum(++numRecordUnits);
            orderedRecordUnits.add(newRecordUnit);
            System.out.println("Number of project record units = " + orderedRecordUnits.size());
            recordCheckBoxFlags.add(false);
            recordCategories.add(newRecordUnit.getParameterValueFromKey(sortOptions.get(3)));
            recordComments.add(newRecordUnit.getParameterValueFromKey(sortOptions.get(4)));
            //update desktopFrame
            System.out.println("Getting new record frame..");
            JInternalFrame recordFrame = createRecordFrame(newRecordUnit.getRecordNum(), newRecordUnit);
            System.out.println("Packing recordFrame..");
            recordFrame.pack();
            System.out.println("Setting record frame visible..");
            recordFrame.setVisible(true);
            System.out.println("Determining location of record frame..");
            recordFrame.setLocation(0, yCoordinate);
            System.out.println("Adding record frame to desktopPane..");
            desktopPane.add(recordFrame);
            System.out.println("Revalidating GUI..");
            System.out.println("yCoordinate = " + yCoordinate);
            yCoordinate +=  RECORD_HEIGHT + 5;
            System.out.println("Number of project record units = " + orderedRecordUnits.size());
            appendMessage = newRecordUnit.getParameterValueFromKey(sortOptions.get(0)) + " record added in the viewer (to be saved).";
        } else {
         	System.out.println("Warning!! The project record " + newRecordUnit.getParameterValueFromKey(sortOptions.get(0)) + " already exists in EphrinSummaryFile.tsv.");
         	appendMessage = "Warning!! The project record " + newRecordUnit.getParameterValueFromKey(sortOptions.get(0)) + " already exists in EphrinSummaryFile.tsv.";
        } 
           desktopPane.setPreferredSize(new Dimension(DESKTOP_PANE_WIDTH, (numRecordUnits + 1) * (RECORD_HEIGHT + 5)));           
           assembleSplitPane1();
           pack();
           setVisible(true);
           revalidate();
           updateEphrinStatus(appendMessage);
    }
    
    private boolean duplicateRecordsCheck(ProjectRecordUnit newRecordUnit) {
    	String newProjectName = newRecordUnit.getParameterValueFromKey(sortOptions.get(0)).trim();
    	for (int i = 0; i < orderedRecordUnits.size(); ++i) {
    		String thisProjectName = orderedRecordUnits.get(i).getParameterValueFromKey(sortOptions.get(0)).trim();
    		if (newProjectName.equals(thisProjectName)) {
    			return true; //signifying duplicate record
    		}
    	}
    	return false; //New record is not a duplicate record
    }
    
    /**
     * Append to existing status of Ephrin
     * @param appendMessage
     */
    public void updateEphrinStatus(String appendMessage) {
    	System.out.println("ViewerFrame::updateEphrinStatus " + appendMessage);
    	currentStatus = "Number of project record units = " + orderedRecordUnits.size(); 
        if (appendMessage != null) {
        	currentStatus += " | | | | " + appendMessage; 
        }
        System.out.println("Setting new currentStatus = " + currentStatus);
        statusPanel.removeAll();
        statusLabel = new JLabel (currentStatus);
        statusLabel.setFont(statusFont);
        statusLabel.setBackground(Color.CYAN);
        statusPanel.setBackground(Color.CYAN);
        statusPanel.add(statusLabel);
        statusPanel.setVisible(true);
        pack();
        setVisible(true);
        revalidate();
    }  
    
    /**
     * Select records for saving to EphrinSummaryFile.tsv
     * If marked == true: Do not select marked records
     * If marked == false: Igonre marked records
     * @return ArrayList of project records to be saved
     */
    
    private ArrayList<ProjectRecordUnit> selectUnmarkedRecordUnits(boolean marked) {
    	ArrayList<ProjectRecordUnit> unmarkedRecordUnits = new ArrayList<ProjectRecordUnit>();
    	for (int i = 0; i < orderedRecordUnits.size(); ++i) {
    		ProjectRecordUnit thisUnit = orderedRecordUnits.get(i);
    		int index = thisUnit.getRecordNum() - 1; 
    		//Update thisUnit with edited comments and categories
			thisUnit.setComment(recordComments.get(index));
			thisUnit.setCategory(recordCategories.get(index));
			if (marked) { //Delete marked records
	    		if (recordCheckBoxFlags.get(index) == false) {
	    			unmarkedRecordUnits.add(thisUnit);
	    			System.out.println("Unit " + thisUnit.getRecordNum() + " is unmarked and added for saving.");
	    		} else {
	    			System.out.println("Unit " + thisUnit.getRecordNum() + " is marked for deletion.");
	    		}
			} else {
    			unmarkedRecordUnits.add(thisUnit);
    			System.out.println("Unit " + thisUnit.getRecordNum() + " is unmarked and added for saving.");
			}
    	}
    	return unmarkedRecordUnits; 
    }
    
    /**
     * Process user input events.
     */
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Corresponding action command is " + evt.getActionCommand() 
                + " evt class = " + evt.getClass());
        //Check whether Details button is pressed - in order to open corresponding hyperlink 
        if (evt.getActionCommand().equals("ImportDirectory")) {
        	String txtDirectory = displayTxtDirectoryChooser();
        	if (txtDirectory != null && owner != null) {
        		owner.notifyNewTxtDirectorySelected(txtDirectory);
        	}
        } else if (evt.getActionCommand().equals("SaveAllRecords")) {
        	ArrayList<ProjectRecordUnit> allRecordUnits = selectUnmarkedRecordUnits(false);
        	owner.notifyOverwriteProjectRecords(allRecordUnits);
        } else if (evt.getActionCommand().equals("DeleteMarkedRecords")) {
        	//Select records to save
        	ArrayList<ProjectRecordUnit> unmarkedRecordUnits = selectUnmarkedRecordUnits(true);
        	owner.notifyOverwriteProjectRecords(unmarkedRecordUnits);
        } else if (evt.getActionCommand().equals("comboBoxChanged")) {
        	if (evt.getSource().getClass().getName().equals("javax.swing.JComboBox")) {
        		JComboBox<String> thisComboBox = (JComboBox<String>) evt.getSource();
                int recordCategoryIndex = Integer.parseInt(thisComboBox.getName()); 
                String selection = (String)thisComboBox.getSelectedItem();
                recordCategories.remove(recordCategoryIndex);
                recordCategories.add(recordCategoryIndex, selection);
                System.out.println("Combo box name = " + thisComboBox.getName() + " Index = " + 
                		recordCategoryIndex + " New value = " + selection);
        	}
        }
    }
    
    /**
     * Remove and nullify all the report units GUI components
     */
    public void cleanVariables() {
        if (recordCheckBoxFlags != null) {
        	recordCheckBoxFlags.clear();
        }
        /*if (recordUnits != null) {
        	recordUnits.clear();
        }*/
        if (orderedRecordUnits != null) {
        	orderedRecordUnits.clear();
        }
        if (recordCategories != null) {
        	recordCategories.clear();
        }
        if (recordComments != null) {
        	recordComments.clear();
        }
    }
    
    public void cleanGUIComponents() {
        if (desktopPane != null) {
            desktopPane.removeAll();
        }
        if (splitPane1 != null) {
        	splitPane1.removeAll();
        }
    }
    
	/**
	 * Display user interface for choosing txt Folder
	 * @return Absolute path of txt Folder selected by user
	 */
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
    public void itemStateChanged(ItemEvent evt) {
        //Find out index of selection, checked-unchecked and update CheckBoxList
        if (evt.getSource().getClass().getName().equals("javax.swing.JCheckBox")) {
            JCheckBox thisCheckBox = (JCheckBox) evt.getSource();
            System.out.println("Check box name = " + thisCheckBox.getName());
            int checkBoxFlagIndex = Integer.parseInt(thisCheckBox.getName());
            //chartCheckBoxFlags will be maintained all the time according to recordNum
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                System.out.print("Selected");
                recordCheckBoxFlags.set(checkBoxFlagIndex, true);
            } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
                System.out.print("DeSelected");
                recordCheckBoxFlags.set(checkBoxFlagIndex, false); 
            }
        }
    }

	@Override
	public void focusGained(FocusEvent arg0) {
	}

	@Override
	public void focusLost(FocusEvent evt) {
		if (evt.getSource().getClass().getName().equals("javax.swing.JTextArea")) {
    		JTextArea thisTextArea = (JTextArea) evt.getSource();
            int recordCommentsIndex = Integer.parseInt(thisTextArea.getName());
            String comment = thisTextArea.getText();
            recordComments.remove(recordCommentsIndex);
            recordComments.add(recordCommentsIndex, comment);
            System.out.println("Comment area name = " + thisTextArea.getName() + " Index = " + 
            		recordCommentsIndex + " New comment = " + comment);
    	}
	}
}

