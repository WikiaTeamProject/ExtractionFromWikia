#!/bin/bash
set dbPediaExtFrmwrkDir=$1
echo "Echoing Variable value"
echo $dbPediaExtFrmwrkDir

#change directory to dbpedia extraction framework directory
cd $dbPediaExtFrmwrkDir

# run extractor
../run extraction extraction-config-file