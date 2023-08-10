package com.modules;


import java.util.HashMap;
import java.util.Objects;

public class MultiMap {
    public static final Object EMPTY = new Object();
    protected static class MyMap extends HashMap<Object,Object>{}
    protected MyMap map;
    public MultiMap(){
        map = new MyMap();
    }
    protected static class Node{
        Object data;
        MyMap map;
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(data, node.data) && Objects.equals(map, node.map);
        }
        @Override
        public int hashCode() {
            return Objects.hash(data, map);
        }
    }
    protected Object put(MyMap m,Object value,Object... keys){
        if(1==keys.length){
            Object key = keys[0];
            Object o = m.get(key);
            if(Objects.isNull(o)){
                Node n = new Node();
                n.data = value;
                m.put(key,n);
                return EMPTY;
            }else{
                if(o instanceof Node n){
                    Object old = n.data;
                    n.data = value;
                    return old;
                }else{
                    throw new IllegalStateException();
                }
            }
        }else{
            Object[] subKeys = new Object[keys.length-1];
            System.arraycopy(keys,1,subKeys,0,subKeys.length);
            final Object key = keys[0];
            Object o = m.get(key);
            if(Objects.isNull(o)){
                Node n = new Node();
                MyMap mm = new MyMap();
                n.map = mm;
                m.put(key,n);
                return put(mm,value,subKeys);
            }else{
                if(o instanceof Node nd){
                    MyMap mm = nd.map;
                    if(Objects.isNull(mm)){
                        mm = new MyMap();
                        nd.map=mm;
                    }
                    return put(mm,value,subKeys);
                }else{
                    throw new IllegalStateException(o.getClass().toString());
                }
            }
        }
    }

    public Object put(Object value,Object...keys){
        return put(map,value,keys);
    }
    protected Object get(MyMap m,Object...keys){
        if(1==keys.length){
            Object key = keys[0];
            Object o = m.get(key);
            if(Objects.nonNull(o)
                    && (o instanceof Node node)){
                    return node.data;
            }else{
                return null;
            }
        }else{
            Object[] subKeys = new Object[keys.length-1];
            System.arraycopy(keys,1,subKeys,0,subKeys.length);
            final Object key = keys[0];
            Object o = m.get(key);
            if(Objects.nonNull(o)){
                if(o instanceof Node n){
                    MyMap myMap = n.map;
                    return Objects.nonNull(myMap)?get(myMap,subKeys):null;
                }else{
                    throw new IllegalStateException(o.getClass().toString());
                }
            }else{
                return null;
            }
        }
    }
    public Object get(Object...keys){
        return get(map,keys);
    }
    static void test0(){
        MultiMap map = new MultiMap();
        map.put("value","1","2");
        map.put("hello","1","2","3","4");
        map.put("hello1","11","2","3","4");
        map.put("hello2","1","2","33","4");
        Object o;
        o = map.get("1", "2");
        System.out.println(o);
        o = map.get("1", "2", "3", "4");
        System.out.println(o);
        o = map.get("11","2","3","4");
        System.out.println(o);
        o = map.get("1","2","33","4");
        System.out.println(o);
    }
    static void test1(){
        MultiMap map = new MultiMap();
        map.put("value","1","2");
        map.put("hello","1","2");
        map.put("hello1","1","2","3");
        Object o;
        o = map.get("1", "2");
        System.out.println(o);
        o = map.get("1", "2", "3");
        System.out.println(o);
        o = map.get("1", "2", "3", "4");
        System.out.println(o);

    }
    public static void test2() {
        MultiMap map = new MultiMap();
        int count = 10;
        for(int i=0;i<count;i++){
            for(int j=0;j<count;j++){
                for(int k=0;k<count;k++){
                    if(i==j&&j==k)continue;
                    Object pre = map.put(""+i+j+k,String.valueOf(i),String.valueOf(j),String.valueOf(k));
                    if(Objects.nonNull(pre))
                        System.out.println(pre);
                }
            }
        }
        Object o;
        for(int i=0;i<count;i++){
            for(int j=0;j<count;j++){
                for(int k=0;k<count;k++){
                    o = map.get(String.valueOf(i),String.valueOf(j),String.valueOf(k));
                    System.out.println(o);
                }
            }
        }
    }

    public static void main(String[] args) {
//        MultiKeyMap k = new MultiKeyMap();
//        test1();
        test2();
    }
}
