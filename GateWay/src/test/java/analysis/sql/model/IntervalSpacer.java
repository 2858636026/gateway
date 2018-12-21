package analysis.sql.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 分割符号
 */
public class IntervalSpacer {
    private static Character add = '+';
    private static Character Less = '-';
    private static Character Multiplication = '*';
    private static Character division = '/';
    private static Character rightBrackets = ')';
    private static Character leftBrackets = '(';
    public static List<Character> ls;

    public static List<Character> kh;

    static {
        ls = new ArrayList<>();
        kh = new ArrayList<>();
        ls.add(add);
        ls.add(Less);
        ls.add(Multiplication);
        ls.add(division);
        ls.add(rightBrackets);
        kh.add(rightBrackets);
        kh.add(leftBrackets);
    }
}
