package edu.sjsu.cmpe.cache.client;
import java.util.*;

public class Client {
    static String[] servers;
    static ConsistentHashC consistentHash;
    
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");
        
        servers = new String[]{"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"};
        consistentHash = new ConsistentHashC();
        
        for(int i=0; i<servers.length; i++){
            consistentHash.add(servers[i]);
        }
        
        //consistentHash.print();
        
        System.out.println("put all keys/value pair...");
        put(1, "a");
        put(2, "b");
        put(3, "c");
        put(4, "d");
        put(5, "e");
        put(6, "f");
        put(7, "g");
        put(8, "h");
        put(9, "i");
        put(10, "j");
        
        System.out.println("retrieve all keys...");
        get(1);
        get(2);
        get(3);
        get(4);
        get(5);
        get(6);
        get(7);
        get(8);
        get(9);
        get(10);
        
        System.out.println("Existing Cache Client...");
    }
    
    static void put(int key, String value){
        int k = key % servers.length;
        CacheServiceInterface cache = consistentHash.bucketLookup(servers[k]);
        
        cache.put(key, value);
        System.out.println("put("+ key +" => "+ value + ")");
    }
    
    
    static void get(int key){
        int k = key % servers.length;
        CacheServiceInterface cache = consistentHash.bucketLookup(servers[k]);
        
        String value = cache.get(key);
        System.out.println("get("+ key +") => " + value);
    }
    

}


