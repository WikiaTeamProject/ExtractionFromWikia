package wikiaStatistics;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * Created by alexandrahofmann on 14.03.17.
 * This is a class on which wikia JSON responses are mapped.
 */
public class ExpandedWikiaItem {

    @JsonProperty("id")
    private int id = 0;

    @JsonProperty("url")
    private String url = "";

    @JsonProperty("domain")
    private String domain = "";

    @JsonProperty("name")
    private String name = "";

    @JsonProperty("headline")
    private String headline = "";

    @JsonProperty("title")
    private String title = "";

    @JsonProperty("lang")
    private String lang = "";

    @JsonProperty("hub")
    private String hub = "";

    @JsonProperty("topic")
    private String topic = "";

    @JsonProperty("desc")
    private String desc = "";

    @JsonProperty("stats")
    private WikiaStats stats = new WikiaStats();

    @JsonProperty("original_dimensions")
    private ImageDimensions original_dimensions = new ImageDimensions();

    @JsonProperty("image")
    private String image = "";

    @JsonProperty("flags")
    private String[] flags;

    @JsonProperty("wam_score")
    private double wam_score = 0.0;

    @JsonProperty("topUsers")
    private String[] topUsers;

    @JsonProperty("wordmark")
    private String wordmark = "";



    public ExpandedWikiaItem() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public WikiaStats getStats() {
        return stats;
    }

    public void setStats(WikiaStats stats) {
        this.stats = stats;
    }

    public ImageDimensions getOriginal_dimensions() {
        return original_dimensions;
    }

    public void setOriginal_dimensions(ImageDimensions original_dimensions) {
        this.original_dimensions = original_dimensions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getFlags() {
        return flags;
    }

    public void setFlags(String[] flags) {
        this.flags = flags;
    }

    public double getWam_score() {
        return wam_score;
    }

    public void setWam_score(double wam_score) {
        this.wam_score = wam_score;
    }

    public String[] getTopUsers() {
        return topUsers;
    }

    public void setTopUsers(String[] topUsers) {
        this.topUsers = topUsers;
    }

    public String getWordmark() {
        return wordmark;
    }

    public void setWordmark(String wordmark) {
        this.wordmark = wordmark;
    }

    @Override
    public String toString() {
        return id +
                ";" + url +
                ";" + domain +
                ";" + name +
                ";" + headline +
                ";" + title +
                ";" + lang +
                ";" + hub +
                ";" + topic +
                ";" + desc +
                ";" + stats +
                ";" + original_dimensions +
                ";" + image +
                ";" + Arrays.toString(flags) +
                ";" + wam_score +
                ";" + Arrays.toString(topUsers) +
                ";" + wordmark;
    }

    public static String getHeader() {
        return  "id" +
                ";url" +
                ";domain" +
                ";name" +
                ";headline" +
                ";title" +
                ";lang" +
                ";hub" +
                ";topic" +
                ";desc" +
                ";" + WikiaStats.getHeader() +
                ";" + ImageDimensions.getHeader() +
                ";image" +
                ";flags" +
                ";wam_score" +
                ";topUsers" +
                ";wordmark";
    }
}
