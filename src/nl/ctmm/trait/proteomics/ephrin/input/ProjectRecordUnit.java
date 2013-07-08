package nl.ctmm.trait.proteomics.ephrin.input;

import nl.ctmm.trait.proteomics.ephrin.utils.Constants;

public class ProjectRecordUnit {

	String projectName = "N/A"; 
	String firstRawFile = "N/A"; 
	String folderPath = "N/A"; 
	String category = Constants.CATEGORY_UNKNOWN; 
	String comment = Constants.NO_COMMENTS_TXT; 
	int recordNum = -1; 
	
	/**
	 * Constructor of ProjectRecordUnit
	 * @param recordNum Number of project record
	 * @param projectName Name of the project
	 * @param firstRawFile First raw file to be displayed
	 * @param folderPath Path of the project folder 
	 */
	public ProjectRecordUnit(int recordNum, String projectName, String firstRawFile, String folderPath,
			String category, String comment) {
		this.recordNum = recordNum; 
		this.projectName = projectName; 
		this.firstRawFile = firstRawFile; 
		this.folderPath = folderPath; 
		this.category = category; 
		this.comment = comment;
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
	
	/**
	 * Retrieve category of project record unit
	 * @return category
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Retrieve category index of project record unit
	 * @return categoryIndex
	 */
	public int getCategoryIndex() {
		if (category.equals(Constants.CATEGORY_HUMAN)) {
			return Constants.CATEGORY_HUMAN_INDEX;
		} else if (category.equals(Constants.CATEGORY_MOUSE)) {
			return Constants.CATEGORY_MOUSE_INDEX;
		} else return Constants.CATEGORY_UNKNOWN_INDEX;
	}
	
	public void setCategoryByIndex(int categoryIndex) {
		if (categoryIndex == Constants.CATEGORY_HUMAN_INDEX) {
			category = Constants.CATEGORY_HUMAN;
		} else if (categoryIndex == Constants.CATEGORY_MOUSE_INDEX) {
			category = Constants.CATEGORY_MOUSE;
		} else category = Constants.CATEGORY_UNKNOWN;
	}
	
	/**
	 * Set category of project record unit
	 * @param category
	 */
	public void setCategory(String category) {
		this.category = category; 
	}
	
	/**
	 * Retrieve comment on project record unit
	 * @return comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Set comment on project record unit
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment; 
	}
}
