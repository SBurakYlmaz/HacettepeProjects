public class Neapolitan extends Pizza {

    @Override
    public String toString() {
        return "Neapolitan ";
    }

    @Override
    public int cost() {
        return 10 + this.MainPizzaTopping.cost();
    }
}
