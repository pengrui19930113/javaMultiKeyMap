package com.test;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
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
        switch (i) {
            case 0, 1 -> {
                return 1;
            }
            default -> {
                    return i * factorial(i - 1);
            }
        }
    }
    /*
        注意 使用多线程并不能提升速度，反而速度变慢了
        而且一旦集合量打了还报错
        [2.548s][warning][os,thread] Failed to start thread "Unknown thread" - pthread_create failed (EAGAIN) for attributes: stacksize: 2048k, guardsize: 16k, detached.
        Exception in thread "Thread-5453" java.lang.OutOfMemoryError: unable to create native thread: possibly out of memory or process/resource limits reached
            at java.base/java.lang.Thread.start0(Native Method)
            at java.base/java.lang.Thread.start(Thread.java:1568)
            at com.test.SomeMath.allSnThread(SomeMath.java:108)
            at com.test.SomeMath.lambda$allSnThread$1(SomeMath.java:105)
            at java.base/java.lang.Thread.run(Thread.java:1623)
     */
    <T extends Comparable<T>> Set<Set<T>> allSnThread(Set<Set<T>> res,Set<T> s){
        if(s.isEmpty())
            return Collections.emptySet();
        final List<Thread> threads = new LinkedList<>();
        for (int i=1;i<=s.size();i++){
            final int ni = i;
            final Thread t = new Thread(()->{
                final Set<Set<T>> sn = sn(ni, s);
                synchronized (res){
                    res.addAll(sn);
                }
            });
            threads.add(t);
            t.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        threads.clear();
        for (T t : s) {
            Thread thread = new Thread(()->{
                final Set<T> t1 = new TreeSet<>(s);
                t1.remove(t);
                allSnThread(res,t1);
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    <T extends Comparable<T>> void allSn(Queue<Set<T>> queue,Set<Set<T>> res){
        Set<T> s;
        while(null!=(s = queue.poll())){
            if(s.isEmpty())
                return;
            for (int i=1;i<=s.size();i++){
                final Set<Set<T>> sn = sn(i, s);
                res.addAll(sn);
            }
            for (T t : s) {
                final Set<T> t1 = new TreeSet<>(s);
                t1.remove(t);
                queue.offer(t1);
            }
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
                res.add(new TreeSet<>(new TreeSet<>(){{add(t);}}));
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

     <T extends Comparable<T>>TypeSets<T> calc(T...num){
        return calc(true,false,num);
     }
     <T extends Comparable<T>>TypeSets<T> calc(boolean queue,boolean thread,T...num){
        if(0==num.length)throw new IllegalArgumentException();
        final Set<T> collect = Arrays.stream(num).collect(Collectors.toSet());
        final Set<Set<T>> all = new HashSet<>();
        if(queue){
            final Queue<Set<T>> q = new LinkedList<>();
            q.offer(collect);
            allSn(q,all);
        }else{
            final Set<Set<T>> sets =thread?allSnThread(all, collect): allSn(all, collect);
        }
        return new TypeSets<>(all);
    }

    public static void test1(String[] args) {
        final boolean thread = false;
        final boolean queue = true;
        SomeMath someMath = new SomeMath();
        final long t = System.currentTimeMillis();
//        TypeSets calc = someMath.calc(1, 2, 3, 4, 5, 6, 7, 8,9);
        Comparable[] params = new Comparable[]{1,2,3,4};
//        TypeSets calc = someMath.calc(params);
        TypeSets<Integer> calc = someMath.calc(queue,thread,1, 2, 3,4,5,6,7,8,9);
        calc.print(System.out);
        System.out.println("length:"+calc.size());//(2^(params.length))-1
        System.out.println("duration:"+(System.currentTimeMillis()-t));
    }

    public static void test2(String[] args) {
        Set<Set<Integer>> sn = new SomeMath().sn(2, Set.of(1, 2, 3,4,5));
        for (Set<Integer> integers : sn) {
            System.out.println(integers);
        }
    }

    public static void main(String[] args) {
//        test2(args);
        test1(args);
    }
}
