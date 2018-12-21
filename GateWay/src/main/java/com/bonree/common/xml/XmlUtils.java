package com.bonree.common.xml;

import com.bonree.common.util.ParamUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 *
 ******************************************************************************/
public class XmlUtils {

    private static Logger log = LoggerFactory.getLogger(XmlUtils.class);

    private Document doc = null;

    /**
     * 初始配置信息对象
     *
     * @param filePath 配置信息路径
     * @throws Exception
     */
    public XmlUtils(String filePath) throws Exception {
        String config = FileUtils.readConfigFile(filePath);
        log.debug(Thread.currentThread().getStackTrace()[1].getLineNumber() + " Get config info for zookeeper: {}", config);
        doc = Jsoup.parse(config);
    }

    /**
     * 概述：获取xml元素对象
     *
     * @param cssQuery 获取元素对象的表达式
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public Element getElement(String cssQuery) throws Exception {
        return doc.select(cssQuery).first();
    }

    /**
     * 概述：获取指定名称的xml元素对象
     *
     * @param element 元素对象
     * @param name    元素的属性名称
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public Element getElement(Element element, String name) throws Exception {
        return element.select(name).first();
    }

    /**
     * 概述：获取指定元素的属性值
     *
     * @param element 元素对象
     * @param name    元素的属性名称
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public String getAttr(Element element, String name) throws Exception {
        return element.attr(name);
    }

    /**
     * 概述：获取指定元素的text值
     *
     * @param element 元素对象
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public String getText(Element element) throws Exception {
        return element.text();
    }

    /**
     * 概述：获取属性对应的值
     *
     * @param element 元素对象
     * @param name    元素的属性名称
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public String getValue(Element element, String name) throws Exception {
        String value = element.select("[name=" + name + "]").val();
        log.info(Thread.currentThread().getStackTrace()[1].getLineNumber() + " Load config info, key: {}, value: {}", name, value);
        return value;
    }

    /**
     * 概述：获取String类型的属性值
     *
     * @param element 元素对象
     * @param name    元素的属性名称
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public String getStringValue(Element element, String name) throws Exception {
        String value = getValue(element, name);
        // 若参数值为空，则抛出异常
        if (value == null || value.trim().equals("")) {
            throw new NullPointerException("获取配置文件中" + name + "键对应的值时，值为空(null)或该键不存在!");
        }
        return value;
    }

    /**
     * 概述：获取Integer类型的属性值
     *
     * @param element 元素对象
     * @param name    元素的属性名称
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public int getIntegerValue(Element element, String name) throws Exception {
        String value = getValue(element, name);
        // 若参数值为空，则抛出异常
        if (value == null || value.trim().equals("")) {
            throw new NullPointerException("获取配置文件中" + name + "键对应的值时，值为空(null)或该键不存在!");
        }
        return Integer.valueOf(value);
    }

    /**
     * 概述：获取Boolean类型的属性值
     *
     * @param element 元素对象
     * @param name    元素的属性名称
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public boolean getBooleanValue(Element element, String name) throws Exception {
        String value = getValue(element, name);
        // 若参数值为空，则抛出异常
        if (value == null || value.trim().equals("")) {
            throw new NullPointerException("获取配置文件中" + name + "键对应的值时，值为空(null)或该键不存在!");
        }
        return Boolean.valueOf(value);
    }

    /**
     * 概述：获取double类型的属性值
     *
     * @param element 元素对象
     * @param name    元素的属性名称
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public double getDoubleValue(Element element, String name) throws Exception {
        String value = getValue(element, name);
        // 若参数值为空，则抛出异常
        if (value == null || value.trim().equals("")) {
            throw new NullPointerException("获取配置文件中" + name + "键对应的值时，值为空(null)或该键不存在!");
        }
        return Double.valueOf(value);
    }

    /**
     * 概述：获取特定集合类型的属性值拼装成字段的值
     *
     * @param element 元素对象
     * @param name    元素的属性名称
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public String getListStr(Element element, String name) throws Exception {
        Element subElement = element.select("[name=" + name + "] list").first();
        List<String> textlist = getListText(subElement);
        // 若参数值为空，则抛出异常
        if (textlist == null || textlist.isEmpty()) {
            throw new NullPointerException("获取配置文件中" + name + "键对应的值时，值为空(null)或该键不存在!");
        }
        log.info(Thread.currentThread().getStackTrace()[1].getLineNumber() + " Load config info, key: {}, value: {}", name, textlist);
        return list2Str(textlist);
    }

    /**
     * 概述：获取特定集合类型的属性值拼装成字段的值
     *
     * @param pattern 元素表达式
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public String getListStr(String pattern) throws Exception {
        List<String> textlist = getListText(pattern);
        // 若参数值为空，则抛出异常
        if (textlist == null || textlist.isEmpty()) {
            throw new NullPointerException("获取配置文件中" + pattern + "键对应的值时，值为空(null)或该键不存在!");
        }
        return list2Str(textlist);
    }

    /**
     * 概述：获取特定集合类型的属性值集合
     *
     * @param element 元素对象
     * @param name    元素的属性名称
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public List<String> getListText(Element element, String name) {
        Element subElement = element.select(name).first();
        List<String> list = new ArrayList<>();
        for (Element propertyElement : subElement.children()) {
            list.add(propertyElement.text());
        }
        log.info(Thread.currentThread().getStackTrace()[1].getLineNumber() + " Load config info, key: {}, value: {}", name, list);
        return list;
    }

    /**
     * 概述：获取特定集合类型的属性值集合
     *
     * @param element 元素对象
     * @param name    元素的属性名称
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public List<Element> getListElement(Element element, String name) {
        Element subElement = element.select(name).first();
        return subElement.children();
    }

    /**
     * 概述：获取特定集合类型的属性值集合
     *
     * @param element 元素对象
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public List<String> getListText(Element element) {
        List<String> list = new ArrayList<>();
        for (Element propertyElement : element.children()) {
            list.add(propertyElement.text());
        }
        return list;
    }

    /**
     * 概述：获取特定集合类型的属性值集合
     *
     * @param pattern 元素表达式
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public List<String> getListText(String pattern) {
        Element element = doc.select(pattern).first();
        List<String> list = new ArrayList<>();
        for (Element propertyElement : element.children()) {
            list.add(propertyElement.text());
        }
        log.info(Thread.currentThread().getStackTrace()[1].getLineNumber() + " Load config info, key: {}, value: {}", pattern, list);
        return list;
    }

    /**
     * 概述：集合转字符串
     *
     * @param list
     * @return
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    private String list2Str(List<String> list) {
        String listStr = "";
        for (String text : list) {
            if ("".equals(listStr)) {
                listStr = text;
            } else {
                listStr += "," + text;
            }
        }
        return listStr;
    }
}
