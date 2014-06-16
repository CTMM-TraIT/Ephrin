package nl.ctmm.trait.proteomics.ephrin.input;

import java.util.List;

public class ProjectRecordUnit {
	private int recordNum = -1;
    private List<String> parameterValues;
    private List<String> parameterNames;
    private List<String> categories;

	/**
	 * Constructor of ProjectRecordUnit
	 * @param recordNum Number of project record
	 */
	public ProjectRecordUnit(int recordNum, List<String> parameterValues, List<String> parameterNames, List<String> categories) {
		this.recordNum = recordNum; 
		this.parameterValues = parameterValues; 
		this.parameterNames = parameterNames; 
		this.categories = categories; 
	}
	
	/**
	 * Set number of project record unit
	 * @param recordNum the record number.
	 */
	public void setRecordNum(int recordNum) {
		this.recordNum = recordNum; 
	}
	
	/**
	 * Retrieve number of project record unit
	 * @return recordNum the record number.
	 */
	public int getRecordNum() {
		return recordNum;
	}
	
    /**
     * Compare this report unit with other report unit based on sorting criteria
     * @param otherUnit Other report unit to be compared
     * @param sortKey Sorting criteria
     * @return if this report unit has higher value return 1, equal value return 0, lower value return -1
     */
    public int compareTo(final ProjectRecordUnit otherUnit, final String sortKey) {
        String thisValue = this.getParameterValueFromKey(sortKey);
        String otherValue = otherUnit.getParameterValueFromKey(sortKey);
        if (thisValue.equals(otherValue)) {
        	return 0; 
        } else if (thisValue.compareTo(otherValue) > 0) { 
            return 1;
        } else if (thisValue.compareTo(otherValue) < 0) { 
            return -1;
        }        return 0; 
    }

	public String getParameterValueFromKey(String paramKey) {
		int parameterIndex = -1; 
		for (int i = 0; i < parameterNames.size(); ++i) {
			String paramName = parameterNames.get(i);
			if (paramName.equals(paramKey)) {
				parameterIndex = i; 
				break; 
			}
		}
		return parameterValues.get(parameterIndex);
	}

	public void setComment(String comment) {
		parameterValues.remove(4); 
		parameterValues.add(4, comment); 
	}

	public void setCategory(String category) {
		parameterValues.remove(3); 
		parameterValues.add(3, category); 
	}
}
