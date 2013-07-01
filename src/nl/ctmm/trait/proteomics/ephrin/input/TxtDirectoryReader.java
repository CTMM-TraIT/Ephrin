package nl.ctmm.trait.proteomics.ephrin.input;

import java.io.File;

import nl.ctmm.trait.proteomics.ephrin.output.SummaryFileWriter;
import nl.ctmm.trait.proteomics.ephrin.utils.Constants;

public class TxtDirectoryReader {
    
    public TxtDirectoryReader (String txtDirectoryName) {
        final File txtDirectory = new File(txtDirectoryName);
        if (txtDirectory.exists()) {
            File txtParent = txtDirectory.getParentFile(); 
            String projectName = txtParent.getParentFile().getName();
            String projectFolder = txtParent.getParentFile().getAbsolutePath();
            String templateFilePath = "";
            String secondLine = "";
            final File[] files = txtDirectory.listFiles();
            if (files != null) {
                for (final File file : files) {
                   if (file.getName().equals(Constants.PROPERTY_TEMPLATE_FILE_NAME)) {
                       System.out.println("Template file found: " + file.getName() + 
                               " Absolute path: " + file.getAbsolutePath());
                       templateFilePath = file.getAbsolutePath();
                       TemplateFileReader templateFileReader = new TemplateFileReader(templateFilePath);
                       secondLine = templateFileReader.readSecondLine();
                       break;
                   }
                }
            }
            System.out.println("Record: " + projectName + " " + secondLine + " " 
                    + projectFolder);
            ProjectRecordUnit projectrecordUnit = new ProjectRecordUnit(0, projectName, secondLine, projectFolder);
            SummaryFileWriter.getInstance().addProjectRecordUnit(projectrecordUnit); 
        }
    }
}
