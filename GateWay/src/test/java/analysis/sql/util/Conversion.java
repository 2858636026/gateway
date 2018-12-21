package analysis.sql.util;

import analysis.sql.model.IntervalSpacer;

import java.util.ArrayList;

public class Conversion {

    /**
     * List的字符集合转换成字符串
     *
     * @param ch
     * @return
     */
    public static String getCharsToString(ArrayList<Character> ch) {
        char[] chs = new char[ch.size()];
        int i = 0;
        for (Character character : ch) {
            chs[i] = character;
            i++;
        }
        return new String(chs);
    }

    /**
     * 去除多空格
     *
     * @param field
     * @return
     */
    public static String removeSpace(String field) {

        while (field.contains("  ")) {
            field = field.replace("  ", " ");
        }
        if (field.charAt(0) != ' ') {//删除第一个空格
            field = "" + field;
        }
        if (field.charAt(field.length() - 1) != ' ') {
            field = field + " ";
        }
        return field;
    }

    /**
     * 添加空间,将数据格式化
     *
     * @param field
     * @return
     */
    public static String addSpace(String field) {
        ArrayList<Character> ch = new ArrayList<>();//保存修改后的串
        Character head;
        Character current;
        ch.add(' ');
        for (int i = 1; i < field.toCharArray().length; i++) {
            head = field.charAt(i - 1);
            current = field.charAt(i);
            if ((IntervalSpacer.ls.contains(head) && !IntervalSpacer.ls.contains(current)) || (!IntervalSpacer.ls.contains(head) && IntervalSpacer.ls.contains(current))) {
                ch.add(' ');
                ch.add(current);
            } else {
                ch.add(current);
            }
        }
        field = getCharsToString(ch);
        //检测左括号
        ArrayList<Character> ch2 = new ArrayList<>();//保存修改后的串
        Character head2;
        Character current2;

        ch2.add(' ');
        for (int i = 1; i < field.toCharArray().length; i++) {
            head2 = field.charAt(i - 1);
            current2 = field.charAt(i);
            boolean b1 = IntervalSpacer.kh.contains(head2);
            boolean b2 = IntervalSpacer.kh.contains(current2);
            if ((b1 && b2) || head2 == ')' || head2 == '(') {
                ch2.add(' ');
                ch2.add(current2);
            } else {
                ch2.add(current2);
            }
        }
        field = removeSpace(getCharsToString(ch2));
        //将方法放在对应的位置

        while (true) {
            int vavle = 0;
            for (int i = 1; i < field.toCharArray().length; i++) {
                Character a = field.charAt(i - 1);
                Character b = field.charAt(i);
                int j = 0;
                if ((b == '(') && (a != ' ')) {
                    vavle = 1;
                    j++;
                    for (int k = i + 1; i < field.toCharArray().length; k++) {
                        if (field.charAt(k) == '(') {
                            j++;
                        }
                        if (field.charAt(k) == ')') {
                            j--;
                        }
                        if (j == 0) {
                            String[] split = field.substring(0, i).split(" ");
                            field = field.substring(0, k + 1) + split[split.length - 1] + field.substring(k + 1, field.length());
                            field = field.substring(0, i - split[split.length - 1].length()) + field.substring(i, field.length());
                            break;
                        }
                    }


                    break;
                }
            }
            if (vavle == 0) {
                break;
            }
        }

        return field;
    }


}
