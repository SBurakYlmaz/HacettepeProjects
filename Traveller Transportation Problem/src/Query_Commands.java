import java.io.*;
import java.util.*;

public class Query_Commands {
    private FileWriter fileWriter;
    private LinkedHashMap<String, Boolean> marked =new LinkedHashMap<>(); /*For each vertex controlling if the vertex visited
    while using the Dfs algorithm*/
    private static boolean no_way=true;

    public static boolean isNo_way() {
        return no_way;
    }

    public static void setNo_way(boolean no_way) {
        Query_Commands.no_way = no_way;
    }

    public FileWriter getFileWriter() {
        return fileWriter;
    }

    public LinkedHashMap<String, Boolean> getMarked() {
        return marked;
    }

    public Query_Commands(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    public void Create_Graph(String path,Graph graph) throws IOException {
        File file = new File(path);
        /*Checking the file existence*/
        if(!file.exists()){
            System.out.println("The file you try to read is not exist\n");
            System.exit(1);
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();

        /*Store the each city_name*/
        ArrayList<String> city_names = new ArrayList<>();

        /*This linked_hash_map for each city storing the neighbours relation For example Istanbul-->00000000*/
        LinkedHashMap<String,String> neighbour_relation =new LinkedHashMap<>();

        while (line != null){
            if(line.contains("Airway")){
                line=br.readLine();
                city_names.clear();
                while (true){
                    String[] temporary_vertex = line.split(" ");
                    if(line.equals("")){
                        line=br.readLine();
                        break;
                    }
                    /*Add each city name into an array-list*/
                    city_names.add(temporary_vertex[0]);

                    /*Store the neighbour relation for airway example Istanbul-->00000000*/
                    neighbour_relation.put(temporary_vertex[0],temporary_vertex[1]);

                    /*Adding Vertex to the graph*/
                    if(!graph.getAdj_list().containsKey(temporary_vertex[0]))
                        graph.Add_Vertex(new Vertex(temporary_vertex[0],new LinkedHashMap<>()));
                    line=br.readLine();
                    if(line==null||line.contains("Railway")||line.contains("Highway")){
                        break;
                    }
                }
                /*Using the neighbour relation (that I stored the relation of each vertex) adding edge to the graphs*/
                for (int i=0;i<neighbour_relation.size();i++){
                    String relation=neighbour_relation.get(city_names.get(i));
                    for(int k=0;k<relation.length();k++){
                        if(relation.charAt(k)=='1'){
                            graph.Add_Edge(city_names.get(i),city_names.get(k),"Airway");
                        }
                    }
                }
            }
            else if(line.contains("Railway")){
                city_names.clear();
                line=br.readLine();
                while (true){
                    String[] temporary_vertex = line.split(" ");
                    if(line.equals("")){
                        line=br.readLine();
                        break;
                    }
                    /*Add each city name into an array-list*/
                    city_names.add(temporary_vertex[0]);

                    /*Store the neighbour relation for railway example Istanbul-->00000000*/
                    neighbour_relation.put(temporary_vertex[0],temporary_vertex[1]);

                    /*Adding Vertex to the graph*/
                    if(!graph.getAdj_list().containsKey(temporary_vertex[0]))
                        graph.Add_Vertex(new Vertex(temporary_vertex[0],new LinkedHashMap<>()));
                    line=br.readLine();
                    if(line==null||line.contains("Airway")||line.contains("Highway")){
                        break;
                    }
                }
                /*Using the neighbour relation (that I stored the relation of each vertex) adding edge to the graphs*/
                for (int i=0;i<neighbour_relation.size();i++){
                    String relation=neighbour_relation.get(city_names.get(i));
                    for(int k=0;k<relation.length();k++){
                        if(relation.charAt(k)=='1'){
                            graph.Add_Edge(city_names.get(i),city_names.get(k),"Railway");
                        }
                    }
                }
            }
            else if(line.contains("Highway")){
                city_names.clear();
                line=br.readLine();
                while (true){
                    String[] temporary_vertex = line.split(" ");
                    if(line.equals("")){
                        line=br.readLine();
                        break;
                    }

                    /*Add each city name into an array-list*/
                    city_names.add(temporary_vertex[0]);

                    /*Store the neighbour relation for highway example Istanbul-->00000000*/
                    neighbour_relation.put(temporary_vertex[0],temporary_vertex[1]);

                    /*Adding Vertex to the graph*/
                    if(!graph.getAdj_list().containsKey(temporary_vertex[0]))
                        graph.Add_Vertex(new Vertex(temporary_vertex[0],new LinkedHashMap<>()));
                    line=br.readLine();
                    if(line==null||line.contains("Railway")||line.contains("Airway")){
                        break;
                    }
                }
                /*Using the neighbour relation (that I stored the relation of each vertex) adding edge to the graphs*/
                for (int i=0;i<neighbour_relation.size();i++){
                    String relation=neighbour_relation.get(city_names.get(i));
                    for(int k=0;k<relation.length();k++){
                        if(relation.charAt(k)=='1'){
                            graph.Add_Edge(city_names.get(i),city_names.get(k),"Highway");
                        }
                    }
                }
            }
        }
        /*Initially marked each vertex as false*/
        for (String city_name : city_names) {
            marked.put(city_name, false);
        }
    }
    public void Reading_Queries(String path,Graph graph) throws IOException{
        File file = new File(path);

        /*Checking the file existence*/
        if(!file.exists()){
            System.out.println("The file you try to read is not exist\n");
            System.exit(1);
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();

        while (line!=null){
            String[] arguments = line.split(" ");

            switch (arguments[0]) {
                case "Q1": {
                    fileWriter.write(String.format("%s, %s, %s, %s, %s\n", arguments[0], arguments[1], arguments[2], arguments[3], arguments[4]));
                    LinkedHashMap<String, String> find_route = new LinkedHashMap<>();
                    graph.Q1_query(arguments[1], arguments[2], arguments[4], Integer.parseInt(arguments[3]), getMarked(), find_route
                            , 0, getFileWriter());
                    if(isNo_way())
                        fileWriter.write("There is no way!\n");
                    setNo_way(true);
                    break;
                }
                case "Q2": {
                    fileWriter.write(String.format("%s, %s, %s, %s\n", arguments[0], arguments[1], arguments[2], arguments[3]));
                    LinkedHashMap<String, String> find_route = new LinkedHashMap<>();
                    graph.Q2_query(arguments[1], arguments[3], arguments[2], find_route, getMarked(), getFileWriter());
                    if(isNo_way())
                        fileWriter.write("There is no way!\n");
                    setNo_way(true);
                    break;
                }
                case "Q3": {
                    fileWriter.write(String.format("%s, %s, %s, %s\n", arguments[0], arguments[1], arguments[2], arguments[3]));
                    LinkedHashMap<String, String> find_route = new LinkedHashMap<>();
                    graph.Q3_query(arguments[1], arguments[2], arguments[3], find_route, getMarked(), getFileWriter());
                    if(isNo_way())
                        fileWriter.write("There is no way!\n");
                    setNo_way(true);
                    break;
                }
                case "Q4": {
                    fileWriter.write(String.format("%s, %s, %s, %s, %s, %s\n", arguments[0], arguments[1], arguments[2], arguments[3]
                            , arguments[4], arguments[5]));
                    LinkedHashMap<String, String> find_route = new LinkedHashMap<>();
                    graph.Q4_query(arguments[1], arguments[2], Character.getNumericValue(arguments[3].charAt(1)),
                            Character.getNumericValue(arguments[4].charAt(1)), Character.getNumericValue(arguments[5].charAt(1))
                            , find_route, getMarked(), getFileWriter(), 0, 0, 0);
                    if(isNo_way())
                        fileWriter.write("There is no way!");
                    setNo_way(true);
                    break;
                }
                case "PRINTGRAPH":
                    fileWriter.write(String.format("%s \n", arguments[0]));
                    graph.Print_Graph(graph.getAdj_list(), getFileWriter());
                    break;
            }

            line=br.readLine();
        }
        fileWriter.close();
    }
}
