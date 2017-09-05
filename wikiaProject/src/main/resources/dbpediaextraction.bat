@echo off
set dbPediaExtFrmwrkDir=%1
echo "Echoing Variable value"
echo %dbPediaExtFrmwrkDir%
REM change directory to dbpedia extraction framework directory
cd %dbPediaExtFrmwrkDir%

REM run extractor
mvn scala:run "-Dlauncher=extraction" "-DaddArgs=extraction.default.properties"