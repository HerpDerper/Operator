package operator.services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ConsoleService {

    @Deprecated
    public static void execute(String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("cmd");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        writer.write(command);
        writer.newLine();
        writer.flush();
        writer.close();
        process.waitFor();
    }

}