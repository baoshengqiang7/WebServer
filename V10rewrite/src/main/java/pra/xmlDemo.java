package pra;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

public class xmlDemo {
    public static void main(String[] args) {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read("./emplist.xml");
            Element root = doc.getRootElement();
            List<Element> list = root.elements();
            for (Element ele:list) {
                List<Element>list1=ele.elements();
                int id=Integer.parseInt(ele.attributeValue("id"));
                System.out.println("员工"+id);
                for (Element e:list1) {
                    System.out.println(e.getName()+": "+e.getText());
                }
                System.out.println();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
