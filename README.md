# point-to-analysis
Point-to-analysis as project 1 of course https://xiongyingfei.github.io/SA/2018/main.htm.

## Introduction

This project concerns the problem of Point-to-Analysis, it is implemented by Java Soot.

As an example, see the following code:

```java
public class Main{
    public static void main(String[] args){
        Benchmark.alloc(1);     // define an alloc site
        A a = new A();
        Benchmark.alloc(2);
        A b = new A();
        Benchmark.alloc(3);
        A c = new A();
        if(args.length > 1) a = b;
        Benchmark.test(1, a);   // query of a pointer
        Benchmark.test(2, c);
    }
}
```

At the query site, the reference `a` can point to either `a` or `b`, since we don't know actual dataflow in runtime. However, query for `c` can only point to `c` itself. In this case, the output to the queries should be:

```
1: 1 2
2: 3
```
