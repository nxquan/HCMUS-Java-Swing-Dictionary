
package main;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;

public class Helper {
    public static String unicodeToASCII(String s) {
        String s1 = Normalizer.normalize(s, Normalizer.Form.NFKD);
        String regex
                = "[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+";
        String s2 = null;
        try {
            s2 = new String(s1.replaceAll(regex, "").getBytes("ascii"),
                    "ascii");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
        return s2;
    }
}
