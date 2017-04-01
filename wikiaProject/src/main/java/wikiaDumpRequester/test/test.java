package wikiaDumpRequester.test;
import java.util.ResourceBundle;
import wikiaDumpRequester.model.WikiaUser;
import wikiaDumpRequester.util.WikiaNewDumpRequest;

/**
 * Created by Samresh Kumar on 4/1/2017.
 */
public class test {

    public static void main(String[] args){

        WikiaUser user=new WikiaUser(ResourceBundle.getBundle("config").getString("username"),
                ResourceBundle.getBundle("config").getString("password"));

        WikiaNewDumpRequest requester=new WikiaNewDumpRequest();
        requester.RequestNewWikiaDump(user.getAccessToken(),"http://bakerstreet.wikia.com/wiki/Special:Statistics" );
        System.out.println(user.getAccessToken());

        System.out.println(ResourceBundle.getBundle("config").getString("username"));
        System.out.println(ResourceBundle.getBundle("config").getString("password"));
    }
}
