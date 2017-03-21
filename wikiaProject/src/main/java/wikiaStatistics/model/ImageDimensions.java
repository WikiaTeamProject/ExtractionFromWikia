package wikiaStatistics.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is a class on which wikia JSON responses are mapped.
 */
public class ImageDimensions {

    @JsonProperty("width")
    int width = 0;

    @JsonProperty("height")
    int height = 0;


    public ImageDimensions() {
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return width + ";" + height;
    }

    /**
     * Creates a Header String with all variables for a csv file
     *
     * @return header string
     */
    public static String getHeader() {
        return  "width" +
                ";height";
    }
}
