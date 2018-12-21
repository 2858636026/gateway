package analysis.sql.impl;

import analysis.sql.IField;

import java.util.List;

/**
 * 字段解析
 */
public class AnalysisFieldImpl implements IField {

    public String method;//方法名称
    public Object leftObj;//左参数
    public String symbol;//连接符号
    public Object rightObj;//右参数
    public String alias;//别名
    public List<Object> objs;//特殊参数


}
