package extraction.model;

/**
 * Created by Samresh Kumar on 5/8/2017.
 * Name: WikiaWikiProperties
 * Description : This class will serve as model to store wikia properties
 */
public class WikiaWikiProperties {

    private String wikiName;
    private String languageCode;
    private String wikiPath;

    //Constructor
    public WikiaWikiProperties(String wikiName,String languageCode,String wikiPath){
        this.wikiName=wikiName;
        this.languageCode=languageCode;
        this.wikiPath=wikiPath;
    }

    //Getter and Setter methods for class attributes
    public String getWikiName(){return this.wikiName;}

    public String getLanguageCode(){return this.languageCode;}

    public String getWikiPath(){return this.wikiPath;}

    public void setWikiName(String wikiName){this.wikiName=wikiName;}

    public void setLanguageCode(String languageCode){ this.languageCode=languageCode;}

    public void setWikiPath(String wikiPath){ this.wikiPath=wikiPath;}
}
