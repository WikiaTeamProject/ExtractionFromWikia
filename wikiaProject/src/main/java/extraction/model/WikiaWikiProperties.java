package extraction.model;

import java.util.Date;
/**
 * This class serves as a structure to store wikia properties
 */
public class WikiaWikiProperties {

    private String wikiName;
    private String languageCode;
    private String wikiPath;
    private Date lastModifiedDate;
    private long wikiSize;
    private String wikiBaseURL;

    //Constructor
    public WikiaWikiProperties(String wikiName,
                               String languageCode,
                               String wikiPath,
                               Date lastModifiedDate,
                               long wikiSize,
                               String wikiBaseURL){
        this.wikiName=wikiName;
        this.languageCode=languageCode;
        this.wikiPath=wikiPath;
        this.lastModifiedDate=lastModifiedDate;
        this.wikiSize=wikiSize;
        this.wikiBaseURL=wikiBaseURL;
    }

    //constructor with no arguments
    public WikiaWikiProperties(){
    }

    //Getter and Setter methods for class attributes
    public String getWikiName(){return this.wikiName;}

    public String getLanguageCode(){return this.languageCode;}

    public String getWikiPath(){return this.wikiPath;}

    public Date getLastModifiedDate(){return this.lastModifiedDate;}

    public long getWikiSize(){return this.wikiSize;}

    public String getWikiBaseURL(){return this.wikiBaseURL;}

    public void setWikiName(String wikiName){this.wikiName=wikiName;}

    public void setLanguageCode(String languageCode){ this.languageCode=languageCode;}

    public void setWikiPath(String wikiPath){ this.wikiPath=wikiPath;}

    public  void setLastModifiedDate(Date modifiedDate){this.lastModifiedDate=modifiedDate;}

    public void setWikiSize(long wikiSize){this.wikiSize=wikiSize;}

    public void setWikiBaseURL(String wikiBaseURL){this.wikiBaseURL=wikiBaseURL;}
}
