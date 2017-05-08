package extraction.model;

/**
 * Created by Samresh Kumar on 5/8/2017.
 * Name: WikiaWikiProperties
 * Description : This class will serve as model to store wikia properties
 */
public class WikiaWikiProperties {

    private String wikiName;
    private String languageCode;

    //Constructor
    public WikiaWikiProperties(String wikiName,String languageCode){
        this.wikiName=wikiName;
        this.languageCode=languageCode;
    }

    //Getter and Setter methods for class attributes
    public String getWikiName(){return this.wikiName;}

    public String getLanguageCode(){return this.languageCode;}

    public void setWikiName(String wikiName){this.wikiName=wikiName;}

    public void setLanguageCode(String languageCode){ this.languageCode=languageCode;}

}
