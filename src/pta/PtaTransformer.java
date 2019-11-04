package pta;

import soot.*;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.*;

import java.util.Map;

public class PtaTransformer extends SceneTransformer {
    private static final int LIST_SIZE = 0x100;

    private String scoi;    // coi stands for "class of interest"
    private ValueBox[] allocSite;
    private Object[] testSite;

    public PtaTransformer(String coi){
        this.scoi = coi;
        this.allocSite = new ValueBox[LIST_SIZE];
        this.testSite = new JimpleLocal[LIST_SIZE];
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
                        testSite[index] = ptr;
                    }
                    else{ /* do nothing */ }
                }
            }
            else if(unit instanceof JAssignStmt){
                if(((JAssignStmt)unit).getRightOp() instanceof JNewExpr){
                    if(allocSite != -1){
                        ValueBox mem = ((JAssignStmt)unit).getLeftOpBox();
                        this.allocSite[allocSite] = mem;
                    }
                }
                else{
                    // other dataflow
                }
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
        System.out.println(testSite.length);
    }
}
