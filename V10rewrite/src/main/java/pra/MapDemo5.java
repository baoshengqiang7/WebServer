package pra;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapDemo5 {
    public static void main(String[] args) {
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("迪达拉",99);
        map.put("斑",98);
        map.put("佐助",97);
        map.put("六道",96);
        map.put("佩恩",95);
        System.out.println(map);

        Set<String> set=map.keySet();
        for (String t:set) {
            System.out.println("key:"+t);
        }

        Collection<Integer> i=map.values();
        for (Integer in:i) {
            System.out.println("value:"+in);
        }

        Set<Map.Entry<String,Integer>> entrySet=map.entrySet();
        for (Map.Entry<String,Integer> e:entrySet) {
            String key=e.getKey();
            int value=e.getValue();
            System.out.println(key+":"+value);
        }

        map.forEach(
                (k,v)->{
                    String line=k+": "+v;
                    System.out.println(line);
                }
        );
    }
}
