package utils;

/**
 * Created by Samresh Kumar on 9/6/2017.
 */
public class OSDetails {
    private static String operatingSystem = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (operatingSystem.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (operatingSystem.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0 || operatingSystem.indexOf("aix") > 0 );
    }

    public static boolean isSolaris() {
        return (operatingSystem.indexOf("sunos") >= 0);
    }

    public static String getNewLineCharacter(){
        if(isWindows())
            return "\r\n";
        else
            return "\n";

    }
}
