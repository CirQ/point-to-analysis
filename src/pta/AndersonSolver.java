package pta;

import soot.jimple.internal.JimpleLocal;

import java.util.*;

public class AndersonSolver implements Solver {
    private Map<JimpleLocal, Integer> allocSite;
    private Map<Integer, JimpleLocal> testSite;
    private Map<JimpleLocal, Set<JimpleLocal>> pointTo;
    private Map<Integer, Set<Integer>> result;

    public AndersonSolver(){
        this.allocSite = new LinkedHashMap<>();
        this.testSite = new LinkedHashMap<>();
        this.pointTo = new LinkedHashMap<>();
        this.result = new TreeMap<>();
    }

    public void addAllocation(int index, JimpleLocal v){
        allocSite.put(v, index);
        pointTo.put(v, new HashSet<>());
        pointTo.get(v).add(v);
    }

    public void addRedirection(JimpleLocal lv, JimpleLocal rv){
        if(!pointTo.containsKey(lv)){
            pointTo.put(lv, new HashSet<>());
            pointTo.get(lv).add(lv);
        }
        pointTo.get(lv).addAll(pointTo.get(rv));
    }

    public void addPointerTest(int index, JimpleLocal jLocal) {
        result.put(index, new TreeSet<>());
        testSite.put(index, jLocal);
    }

    public Map<Integer, Set<Integer>> solve(){
        // todo there should be interation!!!!
        for(Map.Entry<Integer, Set<Integer>> entry: result.entrySet()){
            JimpleLocal jlfrom = testSite.get(entry.getKey());
            for(JimpleLocal jlto: pointTo.get(jlfrom)){
                if(allocSite.containsKey(jlto)){
                    int allocIndex = allocSite.get(jlto);
                    entry.getValue().add(allocIndex);
                }
            }

        }
        return result;
    }
}
