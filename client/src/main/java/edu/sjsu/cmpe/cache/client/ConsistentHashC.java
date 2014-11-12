package edu.sjsu.cmpe.cache.client;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map;

public class ConsistentHashC {
    
    private final int numberOfReplicas = 1;
    private final SortedMap<Integer, CacheServiceInterface> circle =
    new TreeMap<Integer, CacheServiceInterface>();
    
    public void add(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
           CacheServiceInterface cache = new DistributedCacheService(node);
           int key = getHash(node);
           circle.put(key, cache);
        }
       
    }
    
    public void print(){
        for (Integer treeKey : circle.keySet()) {
            System.out.println(circle.get(treeKey));
        }
    }
    
    public void remove(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(getHash(node));
        }
    }
    
    public CacheServiceInterface bucketLookup(String node) {
        if (circle.isEmpty()) {
            return null;
        }
        
        int hash = getHash(node);
        if (!circle.containsKey(hash)) {
            SortedMap<Integer, CacheServiceInterface> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        
       // System.out.println("server => "+hash);
        return circle.get(hash);
    } 
    
    private int getHash(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            return byteArrayToInt(array);
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return 0;
    }
    
    private int byteArrayToInt(byte[] b)
    {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }
    
}