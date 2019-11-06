package pta;

import soot.*;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.*;

import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

public class PtaTransformer extends SceneTransformer {
    private String scoi;    // coi stands for "class of interest"
    private Solver solver;

    public PtaTransformer(String coi){
        this.scoi = coi;
        this.solver = new AndersonSolver();
    }

    private void extractSite(Body body){
        int allocSite = -1;
        for(Unit unit: body.getUnits()){
            if(unit instanceof JInvokeStmt){
                JInvokeStmt invokeS = (JInvokeStmt)unit;
                InvokeExpr invokeE = invokeS.getInvokeExpr();
                if(invokeE instanceof JStaticInvokeExpr){
                    String sig = invokeE.getMethod().getSignature();
                    if(sig.equals("<benchmark.internal.Benchmark: void alloc(int)>")) {
                        allocSite = ((IntConstant)invokeE.getArg(0)).value;
                    }
                    else if(sig.equals("<benchmark.internal.Benchmark: void test(int,java.lang.Object)>")){
                        int index = ((IntConstant)invokeE.getArg(0)).value;
                        JimpleLocal ptr = (JimpleLocal)invokeE.getArg(1);
                        solver.addPointerTest(index, ptr);
                    }
                    else{ /* do nothing */ }
                }
            }
            else if(unit instanceof JAssignStmt){
                if(((JAssignStmt)unit).getRightOp() instanceof JNewExpr){
                    if(allocSite != -1){
                        Value mem = ((JAssignStmt)unit).getLeftOp();
                        if(mem instanceof JimpleLocal)
                            solver.addAllocation(allocSite, (JimpleLocal)mem);
                        else
                            System.err.println("unhandled alloc exception");
                    }
                }
                else{
                    Value lvalue = ((JAssignStmt)unit).getLeftOp();
                    Value rvalue = ((JAssignStmt)unit).getRightOp();
                    if((lvalue instanceof JimpleLocal) && (rvalue instanceof JimpleLocal)){
                        solver.addRedirection((JimpleLocal)lvalue, (JimpleLocal)rvalue);
                    }
                    else{
                        System.err.println("no");
                    }
                }
            }
        }
    }

    private static void printResult(Map<Integer, Set<Integer>> result, PrintStream stream){
        for(Map.Entry<Integer, Set<Integer>> entry: result.entrySet()){
            stream.printf("%d: ", entry.getKey());
            if(entry.getValue().size() == 0){
                stream.println('#');
                continue;
            }
            int count = 0;
            for(Integer i: entry.getValue()){
                stream.print(i);
                char delim = (++count == entry.getValue().size()) ? '\n': ' ';
                stream.print(delim);
            }
        }
    }

    @Override
    protected void internalTransform(String phaseName, Map<String, String> options) {
        for(SootClass sc: Scene.v().getApplicationClasses()){
            if(sc.getName().equals(scoi)){
                for(SootMethod sm: sc.getMethods()){
                    sm.retrieveActiveBody();
                    extractSite(sm.getActiveBody());
                }
            }
        }
        printResult(solver.solve(), System.err);
    }
}
