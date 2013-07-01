package nl.ctmm.trait.proteomics.ephrin.input;

public class ProjectRecordUnit {

	String projectName = "N/A"; 
	String firstRawFile = "N/A"; 
	String folderPath = "N/A"; 
	int recordNum = -1; 
	
	public ProjectRecordUnit(int recordNum, String projectName, String firstRawFile, String folderPath) {
		this.recordNum = recordNum; 
		this.projectName = projectName; 
		this.firstRawFile = firstRawFile; 
		this.folderPath = folderPath; 
	}
	
	public int getRecordNum() {
		return recordNum;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public String getFirstRawFile() {
		return firstRawFile;
	}
	
	public String getFolderPath() {
		return folderPath;
	}
}
