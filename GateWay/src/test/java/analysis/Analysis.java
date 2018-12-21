package analysis;


import analysis.sql.IField;
import analysis.sql.impl.AnalysisFieldImpl;
import analysis.sql.model.Linked;
import analysis.sql.util.Conversion;

import java.util.ArrayList;


//解析字段
public class Analysis {
    public static void main(String[] args) {
        String mess = " 1 +((1 +sum(  a/ sun(12 +23 )+ b  * 3 )))+2   ";
        mess = Conversion.removeSpace(mess);
        System.out.println(Conversion.addSpace(mess));

    }

    public static IField getField(String field) {
        AnalysisFieldImpl analysisField = new AnalysisFieldImpl();
        analysis(analysisField, field);
        return analysisField;
    }

    private static void analysis(AnalysisFieldImpl analysisField, String field) {
        //语法分析,将语法按照后缀的方式排列好
        Linked link = new Linked();
        getLinkedlist(field, link);
        //词法分析,验证参数是否异常

    }

    private static void getLinkedlist(String field, Linked link) {
        //去除多空格为一个空格
        field = Conversion.removeSpace(field);
        char[] chars = field.toCharArray();//字符数组
        ArrayList<Character> word = null;//保存一个元素
        for (char aChar : chars) {

//            word.add(aChar)
        }
        System.out.println(field);

    }
}
