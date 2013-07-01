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
    static final String PROPERTY_EPHRIN_LOGO_FILE = "images\\PDB_2hle_EBI.jpg";
    static final String PROPERTY_PROJECT_LOGO_FILE = "images\\traitctmm.jpg";
	ArrayList<String> SORT_OPTION_NAMES = new ArrayList<String>(
		    Arrays.asList("ProjectName", "FirstRAWFile", "ProjectFolder"));
}
