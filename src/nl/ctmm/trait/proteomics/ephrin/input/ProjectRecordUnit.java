package nl.ctmm.trait.proteomics.ephrin.input;

public class ProjectRecordUnit {

	String projectName = "N/A"; 
	String firstRawFile = "N/A"; 
	String folderPath = "N/A"; 
	
	public ProjectRecordUnit(String projectName, String firstRawFile, String folderPath) {
		this.projectName = projectName; 
		this.firstRawFile = firstRawFile; 
		this.folderPath = folderPath; 
	}
	
}
