package pta;

import soot.PackManager;
import soot.Transform;
import soot.options.Options;

import java.nio.file.FileSystems;
import java.nio.file.Paths;

public class PtaMain {

    // args[0] test.Hello
    public static void main(String[] args){
        String classPath = System.getenv("JAVA_HOME");  // only test on JDK-1.8 with JAVA_HOME set
        String pathjce = Paths.get(classPath, "jre", "lib", "jce.jar").toAbsolutePath().toString();
        String pathrt = Paths.get(classPath, "jre", "lib", "rt.jar").toAbsolutePath().toString();
        String pathcw = FileSystems.getDefault().getPath("src").toAbsolutePath().toString();
        String cp = String.join(";", pathjce, pathrt, pathcw);
        Options.v().set_soot_classpath(cp);
        Options.v().set_whole_program(true);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().setPhaseOption("cg.cha", "enabled:true");

        PackManager.v().getPack("wjtp").add(
                new Transform("wjtp.pta", new PtaTransformer(args[0]))
        );
        soot.Main.main(args);
    }

}
