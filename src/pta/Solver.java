package pta;

import soot.jimple.internal.JimpleLocal;

import java.util.Map;
import java.util.Set;

public interface Solver {
    void addAllocation(int index, JimpleLocal v);
    void addRedirection(JimpleLocal lv, JimpleLocal rv);
    void addPointerTest(int index, JimpleLocal jLocal);
    Map<Integer, Set<Integer>> solve();
}
