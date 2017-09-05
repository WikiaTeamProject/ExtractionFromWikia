In this directory you will find all tests of the project. We use JUnit to test Java coding.
As much as we would like to have a unit test for every method, the test coverage is well
below 100% currently. There is no guarantee that you did not break something when the unit
tests run error-free but it is a good indicator that you did if they do not ;) 

##Test Config File and Test Root
Note that the tests do not use the config file of the actual program but have their own mock. 
You can find it in the [test resources directory](.wikiaProject/src/test/resources). 
In there, a dedicated root directory for tests is specified. <br/>
All files required for tests are located in the [test_files directory](.wikiaProject/src/test/test_files). You will also find the mocked root directory there.

