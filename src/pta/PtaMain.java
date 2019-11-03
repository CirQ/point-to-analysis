package pta;

import soot.options.Options;

import java.nio.file.FileSystems;
import java.nio.file.Paths;

public class PtaMain {

    // args[0] test.Hello
    public static void main(String[] args){
        String classPath = System.getenv("JAVA_HOME");
        String pathjce = Paths.get(classPath, "jre", "lib", "jce.jar").toAbsolutePath().toString();
        String pathrt = Paths.get(classPath, "jre", "lib", "rt.jar").toAbsolutePath().toString();
        String pathcw = FileSystems.getDefault().getPath("src").toAbsolutePath().toString();
        String cp = String.join(";", pathjce, pathrt, pathcw);
        Options.v().set_soot_classpath(cp);

        soot.Main.main(new String[]{
                "-f", "jimple", args[0]
        });
    }

}
