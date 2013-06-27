package nl.ctmm.trait.proteomics.ephrin.gui;

/**
 * InternalFrames: http://docs.oracle.com/javase/tutorial/uiswing/components/internalframe.html
 * Radio buttons: http://www.leepoint.net/notes-java/GUI/components/50radio_buttons/25radiobuttons.html
 * 
 * Swing layout: http://www.cs101.org/courses/fall05/resources/swinglayout/
 */

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.ctmm.trait.proteomics.ephrin.Main;

/**
 * ViewerFrame with the GUI for the QC Report Viewer V2.
 *
 * @author <a href="mailto:pravin.pawar@nbic.nl">Pravin Pawar</a>
 * @author <a href="mailto:freek.de.bruijn@nbic.nl">Freek de Bruijn</a>
 */

public class ViewerFrame extends JFrame implements ActionListener, ItemListener, ChangeListener, MouseListener {
    private static final long serialVersionUID = 1L;
    private JDesktopPane tablePane = new ScrollDesktop();
    private Main owner = null; 
    /**
     * Creates a new instance of the demo.
     * 
     * @param title  the title.
     * @param pipelineStatus 
     */
    public ViewerFrame(final Properties appProperties, final String title, final Main owner) {
        super(title);
        System.out.println("ViewerFrame constructor");
        this.owner = owner; 
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        assembleComponents();
        setResizable(false);
        setVisible(true);
        // Finally refresh the frame.
        revalidate();
    }
    
    
    private void assembleComponents() { 
        System.out.println("ViewerFrame assembleComponents");
        //We need two split panes to create 3 regions in the main frame
        setJMenuBar(createMenuBar());
    }
    
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

