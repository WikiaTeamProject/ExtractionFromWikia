package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Outputs the content of a map to the console.
     * @param map
     * @param <T1>
     * @param <T2>
     */
    public static <T1,T2> void printMap(Map<T1, T2> map){

        for(Map.Entry entry : map.entrySet()){
            System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
        }

    }

}
