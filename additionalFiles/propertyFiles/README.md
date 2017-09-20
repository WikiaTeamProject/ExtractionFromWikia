# Property Files

This folder contains a sample credentials and config file. Each file has two sections. You should have a look at section `To be Specified by User`. 
This section contains default values but also requires your input for a regular program run (like parameter `pathToRootDirectory` for example). Please adapt the parameters to your needs and copy the file into the `wikiaProject/src/main/resources` folder before running the program.<br/>
Section `To be Specified by Expert` contains parameters which all have default values assigned to them.
If you do not want to change the standard behavior of the program, you do not have to do any edits in this section.<br/><br/>

## Credentials.Properties File

### To be Specified by User
`username` & `password`<br/>
These variables represent the credentials for Wikia. If you do not have a Wikia user yet, you need to create one on the Wikia web page.

### To be Specified by Expert
`accessTokenURL`<br/>
This variable represents the access token to login to Wikia. Please do not change it.

## Config.Properties File

### To be Specified by User

`targetnamespace`<br/>
This variable contains the namespace (i.e., domain) you would like to set for your project. 
For our project we chose `dbkwik.webdatacommons.org`. If you want to set another domain name
this is the place to do it. Do not add `http://` or a slash at the end to your domain.
<br/>

`pathToRootDirectory`<br/>
This variable represents the path to your root directory. It contains all output and 
intermediate files from the program operations. It has to be specified, otherwise the 
program will not run. Please refer to our [main guide](./../../README.md) for more
information about how to set up the root directory.<br/>

`Languages`<br/>
This variable is used while creating DBpedia folder structure for the extraction. 
It extracts wikis only the language is defined in this variable. Possible values are, 
for instance, `en` or `fr`. If multiple languages are desired, multiple values can be 
given separated by comma, e.g. `en,fr`.<br/><br/>


### To be Specified by Expert

`includeNullMappings`
This parameter defines whether resources, classes & properties that are not linked 
to DBpedia shall be explicitly mapped to null. The standard value is false. You should
set it to true if you want to evaluate wikis.

`wikiSourceFileName`
This parameter allows you to define the source file name of a wiki for the DBpedia
Extraction Framework. Its standard value is `pages-current.xml`.

