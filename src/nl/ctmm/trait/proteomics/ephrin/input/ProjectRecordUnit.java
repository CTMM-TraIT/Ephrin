package nl.ctmm.trait.proteomics.ephrin.input;

public class ProjectRecordUnit {

	String projectName = "N/A"; 
	String firstRawFile = "N/A"; 
	String folderPath = "N/A"; 
	int recordNum = -1; 
	
	/**
	 * Constructor of ProjectRecordUnit
	 * @param recordNum Number of project record
	 * @param projectName Name of the project
	 * @param firstRawFile First raw file to be displayed
	 * @param folderPath Path of the project folder 
	 */
	public ProjectRecordUnit(int recordNum, String projectName, String firstRawFile, String folderPath) {
		this.recordNum = recordNum; 
		this.projectName = projectName; 
		this.firstRawFile = firstRawFile; 
		this.folderPath = folderPath; 
	}
	
	/**
	 * Set number of project record unit
	 * @param recordNum
	 */
	public void setRecordNum(int recordNum) {
		this.recordNum = recordNum; 
	}
	
	/**
	 * Retrieve number of project record unit
	 * @return recordNum
	 */
	public int getRecordNum() {
		return recordNum;
	}
	
	/**
	 * Retrieve name of the project
	 * @return projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * Retrieve first raw file to be displayed
	 * @return firstRawFile
	 */
	public String getFirstRawFile() {
		return firstRawFile;
	}
	
	/**
	 * Retrive path of the project folder
	 * @return folderPath
	 */
	public String getFolderPath() {
		return folderPath;
	}
}
