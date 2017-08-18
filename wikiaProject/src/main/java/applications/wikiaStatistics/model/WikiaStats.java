package applications.wikiaStatistics.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is a class on which wikia JSON responses are mapped.
 */
public class WikiaStats {

    @JsonProperty("users")
    int users = 0;

    @JsonProperty("articles")
    int articles = 0;

    @JsonProperty("pages")
    int pages = 0;

    @JsonProperty("admins")
    int admins = 0;

    @JsonProperty("activeUsers")
    int activeUsers = 0;

    @JsonProperty("edits")
    int edits = 0;

    @JsonProperty("videos")
    int videos = 0;

    @JsonProperty("images")
    int images = 0;

    @JsonProperty("discussions")
    int discussions = 0;


    public WikiaStats() {
    }


    @Override
    public String toString() {
        return  users +
                ";" + articles +
                ";" + pages +
                ";" + admins +
                ";" + activeUsers +
                ";" + edits +
                ";" + videos +
                ";" + images +
                ";" + discussions;
    }

    /**
     * Creates a Header String with all variables for a csv file
     *
     * @return header string
     */
    public static String getHeader() {
        return  "users" +
                ";articles" +
                ";pages" +
                ";admins" +
                ";activeUsers" +
                ";edits" +
                ";videos" +
                ";images" +
                ";discussions";
    }
}
