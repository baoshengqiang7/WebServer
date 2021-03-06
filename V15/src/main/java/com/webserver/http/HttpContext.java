package com.webserver.http;

import com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 当前类用于保存所有与HTTP协议相关的规定内容以便重用
 */
public class HttpContext {
    /**
     * 资源后缀名与响应头Content-Type值的对应关系
     * key:资源后缀名
     * value:Content-Type对应的值
     */
    private static Map<String,String> mimeMapping = new HashMap<>();

    static {
        initMimeMapping();
    }

    private static void initMimeMapping(){
        try {
             SAXReader reader = new SAXReader();
            Document doc = reader.read("./config/web.xml");
            Element root = doc.getRootElement();
            List<Element> list = root.elements("mime-mapping");
            for(Element web : list){
                String key = web.elementText("extension");
                String value = web.elementText("mime-type");
                mimeMapping.put(key,value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据给定的资源后缀名获取到对应的Content-Type的值
     * @param ext
     * @return
     */
    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }

}
