#!/bin/bash
dbPediaExtFrmwrkDir=$1
extractionPropFile=$2

echo $dbPediaExtFrmwrkDir
echo $extractionPropFile

#change directory to dbpedia extraction framework directory
cd $dbPediaExtFrmwrkDir

# run extractor
../run extraction $extractionPropFile