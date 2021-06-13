import java.util.LinkedHashMap;

public class Vertex {
    private String city_name;
    private LinkedHashMap<String,Edge> current_vertex_neighbours;

    public Vertex(String city_name, LinkedHashMap<String,Edge> current_vertex_neighbours) {
        this.city_name = city_name;
        this.current_vertex_neighbours = current_vertex_neighbours;
    }

    public String getCity_name() {
        return city_name;
    }

    public LinkedHashMap<String,Edge> getCurrent_vertex_neighbours() {
        return current_vertex_neighbours;
    }

    public void add_neighbour(String dest_city,String type){
        switch (type) {
            case "Airway":
                current_vertex_neighbours.put(dest_city, new Edge(false, false, true));
                break;
            case "Highway":
                current_vertex_neighbours.put(dest_city, new Edge(false, true, false));
                break;
            default:
                current_vertex_neighbours.put(dest_city, new Edge(true, false, false));
                break;
        }
    }
}
