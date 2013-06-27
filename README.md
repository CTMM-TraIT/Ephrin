Ephrin
======

Proteomics Project Tracking Software

Goal: To maintain overview of Proteomics projects in one single TSV file based on multiple MaxQuant project configurations.

Given: Each MaxQuant project configuration includes the following: 

1) Project name (Represented by projectFolder)

2) RAW files assigned to a project are listed in file <projectFolder>/combined/txt/experimentalDesignTemplate.txt   

3) Data in experimentalDesignTemplate.txt is organized as follows: 

_________________________________________

Name	Fraction	Experiment

QE1_130212_OPL0000_jurkat2ug_01	1	L01

QE1_130212_OPL0000_jurkat2ug_02	1	L02

QE1_130212_OPL0000_jurkat2ug_03	1	L03

_________________________________________

Requirements for Ephrin: 

1) A user should be able to select txt folder 

2) On selection of txt folder, read experimentalDesignTemplate.txt file

3) From each txt folder, infer Project Name and projectFolder

4) Create a project record as follows: 

__________________________________________________________________________________

ProjectName	 FirstRawFileRecord	projectFolder

QE1_130212_OPL0000	QE1_130212_OPL0000_jurkat2ug_01|1|L01	<projectFolder>

__________________________________________________________________________________

5) Get confirmation from user about saving/discarding above project record

6) In case user decides to save the project record, write record into "EphrinSummaryFile.tsv" file

7) Display all records in JTable format

8) Ability to modify project records from the JTable

9) It is desirable to search project records based on search query from the user. The search query is any string fragment from the project record.

 

