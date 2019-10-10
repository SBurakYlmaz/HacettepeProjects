public class Edge {
    private boolean Railway;
    private boolean Highway;
    private boolean Airway;

    public Edge(boolean railway, boolean highway, boolean airway) {
        Railway = railway;
        Highway = highway;
        Airway = airway;
    }

    public boolean isRailway() {
        return Railway;
    }

    public void setRailway(boolean railway) {
        Railway = railway;
    }

    public boolean isHighway() {
        return Highway;
    }

    public void setHighway(boolean highway) {
        Highway = highway;
    }

    public boolean isAirway() {
        return Airway;
    }

    public void setAirway(boolean airway) {
        Airway = airway;
    }
}
