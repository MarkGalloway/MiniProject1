package MiniProject1;

import java.util.Random;
import java.sql.Date;
/*
 * Utility class with static generic helper methods which belong to
 * no distinct Class. 
 */
public class Utils {

    /*
     * May not be instanced
     */
    private Utils() {
        
    }
    
    /*
     * Generates an SQL format date using the current system date
     */
    public static Date generateDate() {
        return new Date(new java.util.Date().getTime());
    }
    
    /*
     * Generates a random number in the range of 0-9999 and converts it to a string
     * 
     */
    public static String generateRandom() {
        return String.valueOf((new Random()).nextInt(10000));
    }
    
    /*
     * Takes an input string and a max.
     * If the string is shorter than max, it is returned.
     * If the string is longer than max, it is shortened to be
     * max chars long. Simply cuts off the end of strings that are
     * too long for the DB.
     * 
     */
    public static String stringChop(String s, int max) {
        int maxLength = (s.length() < max) ? s.length() 
                                           : max;
        return s.substring(0, maxLength);
    }
    
}
