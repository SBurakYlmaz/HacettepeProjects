import java.io.FileWriter;
import java.io.IOException;

public class Assignment3 {
    public static void main(String[] args) throws IOException {
        Graph graph = new Graph();
        FileWriter fileWriter = new FileWriter(args[2]);
        Query_Commands commander = new Query_Commands(fileWriter);
        commander.Create_Graph(args[0],graph);
        commander.Reading_Queries(args[1],graph);
    }
}
