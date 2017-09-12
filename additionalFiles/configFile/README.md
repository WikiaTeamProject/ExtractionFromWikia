This is a sample config file. It has two sections. You should have a look at section
`To be Specified by User`. This section contains default values but also requires
your input for a regular program run (like parameter `pathToRootDirectory`).
Please adapt the parameters to your needs and copy the file into the 
`wikiaProject/src/main/resources` folder before running the program.<br/>
Section `To be Specified by Expert` contains parameters which all have default values.
If you do not want to change the standard behavior of the program, you do not have to do any
edits in this section.
<br/><br/>

### To be Specified by User

`targetnamespace`<br/>
This variable contains the namespace (i.e., domain) you would like to set for your project. 
For our project we chose `dbkwik.webdatacommons.org`.
<br/>

`pathToRootDirectory`<br/>
This variable represents the path to your root directory. It contains all output and 
intermediate files from the program operations. It has to be specified, otherwise the 
program will not run.<br/>

`Languages`<br/>
This variable is used while creating DBpedia folder structure for applications.extraction. 
It extracts wikis only of languages defined in this variable. Possible values are, 
for instance, `en` or `fr`. If multiple languages are desired, multiple values can be 
given separated by comma, e.g. `en,fr`.<br/><br/>


### To be Specified by Expert



