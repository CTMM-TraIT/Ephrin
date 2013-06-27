package nl.ctmm.trait.proteomics.ephrin.input;

import java.io.File;

public class TxtDirectoryReader {
	
	public TxtDirectoryReader (String txtDirectoryName) {
	    final File txtDirectory = new File(txtDirectoryName);
	    if (txtDirectory.exists()) {
	        final File[] files = txtDirectory.listFiles();
	        if (files != null) {
	            for (final File file : files) {
	               System.out.println("File found " + file.getAbsolutePath());
	            }
	        }
	    }
	}

}
