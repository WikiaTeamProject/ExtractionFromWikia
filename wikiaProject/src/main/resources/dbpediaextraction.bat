@echo off
set dbPediaExtFrmwrkDir=%1
set extractionPropFile=%2
echo "Dbpedia Extractor Path " + %dbPediaExtFrmwrkDir%
echo "Extraction Properties File Path " + %extractionPropFile%
REM change directory to dbpedia extraction framework directory
cd %dbPediaExtFrmwrkDir%

REM run extractor
mvn scala:run "-Dlauncher=extraction" "-DaddArgs=%extractionPropFile%"