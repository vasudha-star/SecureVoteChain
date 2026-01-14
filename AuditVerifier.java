import java.util.*;

public class AuditVerifier {
    Map<String,List<String>> g=new HashMap<>();
    void add(String v,String c){
        g.putIfAbsent(v,new ArrayList<>());
        g.get(v).add(c);
    }
    void print(){
        System.out.println(ConsoleColor.YELLOW+"AUDIT GRAPH:");
        for(String k:g.keySet())
            System.out.println(k+" -> "+g.get(k));
        System.out.println(ConsoleColor.RESET);
    }
}



