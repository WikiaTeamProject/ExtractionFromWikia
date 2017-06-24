package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A helper class for different output operations like printing ArrayList conents on the console.
 */
public class OutputOperations {


    /**
     * Output the content of an array list to the console.
     * @param list
     * @param <T>
     */
    public static <T> void printList(List<T> list){
        for(T t : list){
            System.out.println(t.toString());
        }
    }

}
