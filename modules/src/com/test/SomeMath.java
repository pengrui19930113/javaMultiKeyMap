package com.test;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class SomeMath {

    static class TypeSets<T extends Comparable<T>>{
        List<TypeSet<T>> c = new ArrayList<>();
        public TypeSets(Set<Set<T>> sets) {
            for (Set<T> integers : sets) {
                boolean add = c.add(new TypeSet<T>(integers));
//                System.out.println(add);
            }
            Collections.sort(c);
        }
        void print(PrintStream ps){
            for (TypeSet<T> typeSet : c) {
                typeSet.print(ps);
            }
        }
        int size(){return c.size();}
    }
    static class TypeSet<T extends Comparable<T>> implements Comparable<TypeSet<T>>{
        TreeSet<T> set = new TreeSet<>();
        public TypeSet(Set<T> s) {
            set.addAll(s);
        }
        void print(PrintStream ps){
            ps.println(set);
        }
        @Override
        public int compareTo(TypeSet<T> o) {
            if(this.set.size()!=o.set.size()){
                return this.set.size()-o.set.size();
            }else{
                final List<T> ta = new ArrayList<>(this.set);
                final List<T> oa = new ArrayList<>(o.set);
                for(int i=0;i<ta.size();i++){
                    final T tt = ta.get(i);
                    final T oo = oa.get(i);
                    final int res = tt.compareTo(oo);
                    if(0!=tt.compareTo(oo))
                        return res;
                }
                return 0;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TypeSet<?> typeSet = (TypeSet<?>) o;
            return Objects.equals(set, typeSet.set);
        }

        @Override
        public int hashCode() {
            return Objects.hash(set);
        }
    }
    int factorial(int i){
        if(i<0)throw new IllegalArgumentException();
        switch (i){
            case 0,1:{return 1;}
            default: return i*factorial(i-1);
        }
    }

    <T extends Comparable<T>> Set<Set<T>> allSn(Set<Set<T>> res,Set<T> s){
        if(s.isEmpty())
            return Collections.emptySet();
        for (int i=1;i<=s.size();i++){
            final Set<Set<T>> sn = sn(i, s);
            res.addAll(sn);
        }
        for (T t : s) {
            final Set<T> t1 = new TreeSet<>(s);
            t1.remove(t);
            allSn(res,t1);
        }
        return res;
    }
    //s 中任意取n个元素组成的集合的集合
    <T extends Comparable<T>> Set<Set<T>> sn(int n,Set<T> remain){
            if(n<1)throw new IllegalArgumentException();
            Set<Set<T>> res = new HashSet<>();
            if(1==n){
                for (T t : remain) {
                    Set<T> tmp = new TreeSet<>(){{add(t);}};
                    res.add(new TreeSet<>(tmp));
                }
            }else{
                if(n==remain.size())
                    res.add(new TreeSet<>(remain));
                else{
                    for (T t : remain) {
                        Set<T> tmp = new TreeSet<>(remain);
                        tmp.remove(t);
                        Set<Set<T>> sets = sn(n - 1, tmp);
                        for (Set<T> set : sets) {
                            set.add(t);
                            res.add(set);
                        }
                    }
                }
            }
            return res;
    }


     <T extends Comparable<T>>TypeSets calc(T...num){
        if(0==num.length)throw new IllegalArgumentException();
        final Set<T> collect = Arrays.stream(num).collect(Collectors.toSet());
        final Set<Set<T>> all = new HashSet<>();
        final Set<Set<T>> sets = allSn(all, collect);
        System.out.println(sets == all);
        final TypeSets res = new TypeSets(all);
        return res;
    }

    public static void test1(String[] args) {
        SomeMath someMath = new SomeMath();
        final long t = System.currentTimeMillis();
//        TypeSets calc = someMath.calc(1, 2, 3, 4, 5, 6, 7, 8,9);
        Comparable[] params = new Comparable[]{1,2,3,4};
//        TypeSets calc = someMath.calc(params);
        TypeSets calc = someMath.calc(1,2,3,4);
        calc.print(System.out);
        System.out.println("length:"+calc.size());//(2^(params.length))-1
        System.out.println("duration:"+(System.currentTimeMillis()-t));
    }

    public static void test2(String[] args) {
        Set<Set<Integer>> sn = new SomeMath().sn(2, Set.of(1, 2, 3,4,5,6,7));
        for (Set<Integer> integers : sn) {
            System.out.println(sn);
        }
    }

    public static void main(String[] args) {
        test1(args);
    }
}
