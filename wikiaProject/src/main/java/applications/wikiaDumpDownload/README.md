**WikiaDumpDownloadApplication**<br/>
This application downloads either all wiki dumps on Wikia existing for the language code specified in the `resources/config.properties` file or only a specified list of URLs of wikis.

(1) If you want to download all existing dumps of a specific language, then please run [wikiaStatisticsApplication](/applications/wikiaStatistics/WikiaStatisticsApplication.java) to retrieve a file including all URLs of wikis on Wikia.

(2) If you want to download only a list of Wikia wikis, e.g. for testing, you can use the uncommented lines under (2) and specify the URLs for which you want to download dumps.
