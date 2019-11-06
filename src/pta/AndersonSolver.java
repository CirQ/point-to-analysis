package pta;

import soot.ValueBox;
import soot.jimple.internal.JimpleLocal;

import java.util.*;

public class AndersonSolver implements Solver {
    private Map<Integer, ValueBox> allocSite;
    private Map<Integer, JimpleLocal> testSite;
    private Map<Integer, Set<Integer>> result;


    public AndersonSolver(){
        this.allocSite = new LinkedHashMap<>();
        this.testSite = new LinkedHashMap<>();
        this.result = new TreeMap<>();
    }

    public void addAllocation(int index, ValueBox v){

    }

    public void addRedirection(ValueBox lv, ValueBox rv){

    }

    public void addPointerTest(int index, JimpleLocal jLocal) {
        result.put(index, new TreeSet<>());

    }

    public Map<Integer, Set<Integer>> solve(){
        return result;
    }
}
