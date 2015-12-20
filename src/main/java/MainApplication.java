import model.reader.FxmlGenerator;
import utils.OsUtils;
import utils.StringUtils;

import java.io.File;
import java.util.List;

import static java.lang.System.out;

public class MainApplication {
    public static void main(String[] args){

        MainApplication application = new MainApplication();
        application.run(args);
    }

    void run(String[] args){
        String path = "../DeskTools/src/";
        if (args.length > 0) {
            path = args[0];
        }
        String[]  files = OsUtils.GetDirectoryFiles(path, true,
                file->file.getName().endsWith(".fxml"));
        for (String file : files) {
            if (file.endsWith(".fxml")) {
                out.println("To compile: " + file);

                compile(file);
            }
        }
    }

    void compile(String file) {
        FxmlGenerator processor = new FxmlGenerator(file);
        File fileData = new File(file);
        String packageName = getPackageName(fileData.getParent());
        String className = StringUtils.substringBeforeLast(fileData.getName(), ".");
        processor.process("Fx" + StringUtils.indent(className), packageName);
    }
    String getPackageName(String path)  {
        String[] files = OsUtils.GetDirectoryFiles(path, false, file->
                (file.getName().endsWith(".java")
                        || file.getName().endsWith(".kt"))
                && (!file.getName().startsWith("Fx"))
        );


        for (String file : files) {
            List<String> lines = OsUtils.readAllLines(file);
            for (String line : lines) {
                String lineTrimmed = line.trim();
                if (!lineTrimmed.startsWith("package")) {
                    continue;
                }
                String packageLine =
                        StringUtils.removeSuffix(
                                StringUtils.removePrefix(lineTrimmed, "package"),
                                ";");
                return packageLine;
            }
        }
        return "";
    }
}
