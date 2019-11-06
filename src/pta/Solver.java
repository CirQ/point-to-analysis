package pta;

import soot.ValueBox;
import soot.jimple.internal.JimpleLocal;

import java.util.List;
import java.util.Map;

public interface Solver {
    void addAllocation(int index, ValueBox v);
    void addRedirection(ValueBox lv, ValueBox rv);
    void addPointerTest(int index, JimpleLocal jLocal);
    Map<Integer, List<Integer>> solve();
}
