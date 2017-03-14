package wikiaStatistics;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by alexandrahofmann on 14.03.17.
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

    public static String getHeader() {
        return  "width" +
                ";height";
    }
}
