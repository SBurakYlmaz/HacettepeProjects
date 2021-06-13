import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Graph {
    private Map<String, Vertex> adj_list;

    public Graph() {
        this.adj_list = new LinkedHashMap<String, Vertex>();
    }

    public Map<String, Vertex> getAdj_list() {
        return adj_list;
    }

    public void Add_Vertex(Vertex vertex) {
        adj_list.put(vertex.getCity_name(), vertex);
    }

    public void Add_Edge(String Source_city, String Dest_city, String type) {
        if (adj_list.get(Source_city).getCurrent_vertex_neighbours().containsKey(Dest_city)) {
            switch (type) {
                case "Airway":
                    adj_list.get(Source_city).getCurrent_vertex_neighbours().get(Dest_city).setAirway(true);
                    break;
                case "Highway":
                    adj_list.get(Source_city).getCurrent_vertex_neighbours().get(Dest_city).setHighway(true);
                    break;
                default:
                    adj_list.get(Source_city).getCurrent_vertex_neighbours().get(Dest_city).setRailway(true);
                    break;
            }
        } else
            adj_list.get(Source_city).add_neighbour(Dest_city, type);
    }

    public void Q1_query(String Source_city, String Dest_city, String type, Integer count, LinkedHashMap<String, Boolean> marked
            , LinkedHashMap<String, String> find_route, int type_count, FileWriter fileWriter) throws IOException {

        if (Source_city.equals(Dest_city)) {
            if (type_count >= count) {
                Query_Commands.setNo_way(false);
                for (Map.Entry<String, String> entry : find_route.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    fileWriter.write(String.format("%s, %s, ", key, value));
                }
                fileWriter.write(Dest_city);
                fileWriter.write("\n");
            }
            return;
        }
        /*Marked as true for the vertex we are about the discover*/
        marked.put(Source_city, true);

        for (Map.Entry<String, Edge> entry : adj_list.get(Source_city).getCurrent_vertex_neighbours().entrySet()) {
            String key = entry.getKey();
            Edge value = entry.getValue();
            if (!(marked.get(key))) {

                /*Checking if the edge airway exist*/
                if (value.isAirway()) {
                    /*If the type we are looking for is the right way of travel then we increment the type_count
                     * While doing that we are make sure that we provide the requested time of using the right way of travel edge
                     * (airway,highway or railway)*/
                    if (type.equals("A"))
                        type_count++;

                    /*Put the route into the find route linked-hash-map with the corresponding type of edge*/
                    find_route.put(Source_city, "A");
                    Q1_query(key, Dest_city, type, count, marked, find_route, type_count, fileWriter);

                    /*We are make sure that when we discover a vertex with requested type after removing the edge
                     * for other search operations we have to decrement the type*/
                    if (type.equals("A"))
                        type_count--;
                }
                if (value.isRailway()) {

                    /*Checking if the edge railway exist*/
                    /*If the type we are looking for is the right way of travel then we increment the type_count
                     * While doing that we are make sure that we provide the requested time of using the right way of travel edge
                     * (airway,highway or railway)*/
                    if (type.equals("R"))
                        type_count++;

                    /*Put the route into the find route linked-hash-map with the corresponding type of edge*/
                    find_route.put(Source_city, "R");
                    Q1_query(key, Dest_city, type, count, marked, find_route, type_count, fileWriter);

                    /*We are make sure that when we discover a vertex with requested type after removing the edge
                     * for other search operations we have to decrement the type*/
                    if (type.equals("R"))
                        type_count--;
                }
                if (value.isHighway()) {

                    /*Checking if the edge highway exist*/
                    /*If the type we are looking for is the right way of travel then we increment the type_count
                     * While doing that we are make sure that we provide the requested time of using the right way of travel edge
                     * (airway,highway or railway)*/
                    if (type.equals("H"))
                        type_count++;

                    /*Put the route into the find route linked-hash-map with the corresponding type of edge*/
                    find_route.put(Source_city, "H");
                    Q1_query(key, Dest_city, type, count, marked, find_route, type_count, fileWriter);

                    /*We are make sure that when we discover a vertex with requested type after removing the edge
                     * for other search operations we have to decrement the type*/
                    if (type.equals("H"))
                        type_count--;
                }
            }
        }

        find_route.remove(Source_city);
        marked.put(Source_city, false);

    }

    public void Q2_query(String Source_city, String Middle_city, String Dest_city, LinkedHashMap<String, String> find_route,
                         LinkedHashMap<String, Boolean> marked, FileWriter fileWriter) throws IOException {

        /*If the middle-city are in our find route linked-hash-map that means we find a way from our source_city to dest_city
         * passing from the requested city*/
        if (Source_city.equals(Dest_city)) {
            if (find_route.containsKey(Middle_city)) {
                Query_Commands.setNo_way(false);
                for (Map.Entry<String, String> entry : find_route.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    fileWriter.write(String.format("%s, %s, ", key, value));
                }
                fileWriter.write(Dest_city);
                fileWriter.write("\n");
            }
            return;
        }

        /*Marked as true for the vertex we are about the discover*/
        marked.put(Source_city, true);

        for (Map.Entry<String, Edge> entry : adj_list.get(Source_city).getCurrent_vertex_neighbours().entrySet()) {
            String key = entry.getKey();
            Edge value = entry.getValue();
            if (!(marked.get(key))) {

                /*Checking if the edge airway exist*/
                if (value.isAirway()) {
                    /*Put the route into the find route linked-hash-map with the corresponding type of edge*/
                    find_route.put(Source_city, "A");
                    Q2_query(key, Middle_city, Dest_city, find_route, marked, fileWriter);
                }
                /*Checking if the edge railway exist*/
                if (value.isRailway()) {
                    /*Put the route into the find route linked-hash-map with the corresponding type of edge*/
                    find_route.put(Source_city, "R");
                    Q2_query(key, Middle_city, Dest_city, find_route, marked, fileWriter);

                }
                /*Checking if the edge highway exist*/
                if (value.isHighway()) {
                    /*Put the route into the find route linked-hash-map with the corresponding type of edge*/
                    find_route.put(Source_city, "H");
                    Q2_query(key, Middle_city, Dest_city, find_route, marked, fileWriter);
                }
            }
        }
        find_route.remove(Source_city);
        marked.put(Source_city, false);
    }

    public void Q3_query(String Source_city, String Dest_city, String type, LinkedHashMap<String, String> find_route,
                         LinkedHashMap<String, Boolean> marked, FileWriter fileWriter) throws IOException {
        if (Source_city.equals(Dest_city)) {
            Query_Commands.setNo_way(false);
            for (Map.Entry<String, String> entry : find_route.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                fileWriter.write(String.format("%s, %s, ", key, value));
            }
            fileWriter.write(Dest_city);
            fileWriter.write("\n");
            return;
        }

        /*Marked as true for the vertex we are about the discover*/
        marked.put(Source_city, true);

        for (Map.Entry<String, Edge> entry : adj_list.get(Source_city).getCurrent_vertex_neighbours().entrySet()) {
            String key = entry.getKey();
            Edge value = entry.getValue();
            if (!(marked.get(key))) {

                switch (type) {
                    case "A":
                        /*Checking if the edge airway exist*/
                        if (value.isAirway()) {
                            /*Put the route into the find route linked-hash-map with the corresponding type of edge*/
                            find_route.put(Source_city, "A");
                            Q3_query(key, Dest_city, type, find_route, marked, fileWriter);
                        }
                        break;
                    case "R":
                        /*Checking if the edge railway exist*/
                        if (value.isRailway()) {
                            /*Put the route into the find route linked-hash-map with the corresponding type of edge*/
                            find_route.put(Source_city, "R");
                            Q3_query(key, Dest_city, type, find_route, marked, fileWriter);

                        }
                        break;
                    default:
                        /*Checking if the edge highway exist*/
                        if (value.isHighway()) {
                            /*Put the route into the find route linked-hash-map with the corresponding type of edge*/
                            find_route.put(Source_city, "H");
                            Q3_query(key, Dest_city, type, find_route, marked, fileWriter);
                        }
                        break;
                }

            }
        }
        find_route.remove(Source_city);
        marked.put(Source_city, false);
    }

    public void Q4_query(String Source_city, String Dest_city, Integer air_count_info,
                         Integer high_count_info, Integer rail_count_info,
                         LinkedHashMap<String, String> find_route, LinkedHashMap<String, Boolean> marked, FileWriter fileWriter,
                         int airway_count, int highway_count, int railway_count) throws IOException {
        if (Source_city.equals(Dest_city)) {
            if (airway_count == air_count_info && railway_count == rail_count_info && highway_count == high_count_info) {
                Query_Commands.setNo_way(false);
                for (Map.Entry<String, String> entry : find_route.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    fileWriter.write(String.format("%s, %s, ", key, value));
                }
                fileWriter.write(Dest_city);
                fileWriter.write("\n");
            }
            return;
        }
        /*Marked as true for the vertex we are about the discover*/
        marked.put(Source_city, true);

        for (Map.Entry<String, Edge> entry : adj_list.get(Source_city).getCurrent_vertex_neighbours().entrySet()) {
            String key = entry.getKey();
            Edge value = entry.getValue();
            if (!(marked.get(key))) {

                /*Checking if the edge airway exist*/
                if (value.isAirway()) {
                    /*If the type we are looking for is the right way of travel then we increment the type_count
                     * While doing that we are make sure that we provide the requested time of using the right way of travel edge
                     * (airway,highway or railway)*/
                    airway_count++;

                    /*Put the route into the find route linked-hash-map with the corresponding type of edge*/
                    find_route.put(Source_city, "A");
                    Q4_query(key, Dest_city, air_count_info, high_count_info
                            , rail_count_info, find_route, marked, fileWriter, airway_count, highway_count, railway_count);

                    /*We are make sure that when we discover a vertex with requested type after removing the edge
                     * for other search operations we have to decrement the type*/
                    airway_count--;
                }
                if (value.isRailway()) {

                    /*Checking if the edge railway exist*/
                    /*If the type we are looking for is the right way of travel then we increment the type_count
                     * While doing that we are make sure that we provide the requested time of using the right way of travel edge
                     * (airway,highway or railway)*/
                    railway_count++;

                    /*Put the route into the find route linked-hash-map with the corresponding type of edge*/
                    find_route.put(Source_city, "R");
                    Q4_query(key, Dest_city, air_count_info, high_count_info
                            , rail_count_info, find_route, marked, fileWriter, airway_count, highway_count, railway_count);

                    /*We are make sure that when we discover a vertex with requested type after removing the edge
                     * for other search operations we have to decrement the type*/
                    railway_count--;
                }
                if (value.isHighway()) {

                    /*Checking if the edge highway exist*/
                    /*If the type we are looking for is the right way of travel then we increment the type_count
                     * While doing that we are make sure that we provide the requested time of using the right way of travel edge
                     * (airway,highway or railway)*/
                    highway_count++;
                    /*Put the route into the find route linked-hash-map with the corresponding type of edge*/
                    find_route.put(Source_city, "H");
                    Q4_query(key, Dest_city, air_count_info, high_count_info
                            , rail_count_info, find_route, marked, fileWriter, airway_count, highway_count, railway_count);

                    /*We are make sure that when we discover a vertex with requested type after removing the edge
                     * for other search operations we have to decrement the type*/
                    highway_count--;
                }
            }
        }

        find_route.remove(Source_city);
        marked.put(Source_city, false);
    }

    /*Printing the graph that we stored as adj-list*/
    public void Print_Graph(Map<String, Vertex> adj_list, FileWriter fileWriter) throws IOException {
        for (Map.Entry<String, Vertex> entry : adj_list.entrySet()) {
            String key = entry.getKey();
            Vertex value = entry.getValue();
            fileWriter.write(String.format("%s --> ", key));
            for (String keys : value.getCurrent_vertex_neighbours().keySet()) {
                fileWriter.write(String.format("%s ", keys));
            }
            fileWriter.write("\n");
        }
    }
}
