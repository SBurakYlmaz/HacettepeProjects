import java.io.FileWriter;
import java.io.IOException;

public class Exp2 {
    public static void main(String[] args) throws IOException {
        if(args.length != 2)
        {
            System.out.println("Not enough or too many arguments");
            System.exit(0);
        }
        FileWriter fileWriter = new FileWriter(args[1]);
        Commands commander = new Commands(fileWriter,null);
        commander.reader_command(args[0]);
    }
}
