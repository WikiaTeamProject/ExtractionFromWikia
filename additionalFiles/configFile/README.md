This is a sample config file.
Please adapt the parameters to your needs and copy the file into the `wikiaProject/src/main/resources` folder before running the program.<br/>

`targetnamespace`<br/>
This variable contains the namespace (i.e., domain) you would like to set for your project. For our project we chose `dbkwik.webdatacommons.org`.
<br/><br/>

`pathToRootDirectory`<br/>
This variable represents the path to your root directory. It contains all output and intermediate files from the program operations. It has to be specified, otherwise the program will not run.<br/><br/>

`Languages`<br/>
This variable is used while creating DBpedia folder structure for applications.extraction. It extracts wikis only of languages defined in this variable. Possible values are, for instance, `en` or `fr`. If multiple languages are desired, multiple values can be given separated by comma, e.g. `en,fr`.<br/><br/>

TODO: explain other variables

