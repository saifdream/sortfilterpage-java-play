package utils;


public class StringUtil {

    public static String tidyUp(String input) {
        if(input == null) { return null; }
        String value = input.trim();
        return (value.isEmpty() ? null : value);
    }

}
