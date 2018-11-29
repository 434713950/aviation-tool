package com.github.tool.analysis;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @Author: PengCheng
 * @Description:
 * @Date: 2018/7/20
 */
public class XmlUtil {

    /**
     * 将xml解析成bean
     * @param xml
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parserXmlToBean(String xml,Class<T> clazz){
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(clazz);
        T obj = (T) xstream.fromXML(xml);
        return obj;
    }

    /**
     * 将bean解析成xml
     * @param obj
     * @param clazz
     * @return
     */
    public static String parserBeanToXml(Object obj,Class clazz){
        XStream xstream = new XStream();
        xstream.processAnnotations(clazz);
        xstream.autodetectAnnotations(true);
        return xstream.toXML(obj);
    }
}
