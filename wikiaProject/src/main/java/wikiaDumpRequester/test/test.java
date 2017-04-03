package wikiaDumpRequester.test;
import java.util.ArrayList;
import java.util.ResourceBundle;

import utils.Utils;
import wikiaDumpRequester.model.WikiaUser;
import wikiaDumpRequester.util.WikiaNewDumpRequest;

/**
 * Created by Samresh Kumar on 4/1/2017.
 */
public class test {

    public static void main(String[] args){

        WikiaUser user=new WikiaUser(ResourceBundle.getBundle("config").getString("username"),
                ResourceBundle.getBundle("config").getString("password"));
        String filepath = ResourceBundle.getBundle("config").getString("directory") + "/wikiaAllOverview.csv";


        WikiaNewDumpRequest requester=new WikiaNewDumpRequest();
        ArrayList<String> urls = Utils.getUrls(filepath);

        String token = user.getAccessToken();
        System.out.println(token);

//        for (String url : urls) {
//            System.out.println(url);
//            requester.RequestNewWikiaDump(token, url);
//        }


        System.out.println(ResourceBundle.getBundle("config").getString("username"));
        System.out.println(ResourceBundle.getBundle("config").getString("password"));
    }
}
