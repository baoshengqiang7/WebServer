package http;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpContext {
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
            System.out.println(mimeMapping.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }

}

