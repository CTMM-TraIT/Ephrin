package nl.ctmm.trait.proteomics.ephrin.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This interface contains the most important constants of the project.
 *
 * @author <a href="mailto:pravin.pawar@nbic.nl">Pravin Pawar</a>
 * @author <a href="mailto:freek.de.bruijn@nbic.nl">Freek de Bruijn</a>
 */
public interface Constants {
    static final String APPLICATION_NAME = "OPL Project tRACKER";
    static final String APPLICATION_VERSION = "0.0.1";
    static final String PROPERTIES_FILE_NAME = "appProperties";
    static final String PROPERTY_TEMPLATE_FILE_NAME = "experimentalDesignTemplate.txt";
    static final String PROPERTY_SUMMARY_FILE_FULLPATH = "EphrinSummaryFile.tsv";
    static final String PROPERTY_BACKUP_FILE_FULLPATH = "EphrinSummaryFile.tsv.bak";
    static final String PROPERTY_EPHRIN_LOGO_FILE = "images\\PDB_2hle_EBI.jpg";
    static final String PROPERTY_PROJECT_LOGO_FILE = "images\\traitctmm.jpg";
    static final String[] CATEGORY_NAMES = {"Mouse", "Human", "Unknown"};
    static final int[] CATEGORY_INDEX = {0, 1, 2};
    static final String CATEGORY_MOUSE = "Mouse";
    static final String CATEGORY_HUMAN = "Human";
    static final String CATEGORY_UNKNOWN = "Unknown";
    static final int CATEGORY_MOUSE_INDEX = 0; 
    static final int CATEGORY_HUMAN_INDEX = 1; 
    static final int CATEGORY_UNKNOWN_INDEX = 2;
    static final String NO_COMMENTS_TXT = "No comments yet.";
	ArrayList<String> SORT_OPTION_NAMES = new ArrayList<String>(
		    Arrays.asList("ProjectName", "FirstRAWFile", "ProjectFolder", "Category", "Comment"));
}
