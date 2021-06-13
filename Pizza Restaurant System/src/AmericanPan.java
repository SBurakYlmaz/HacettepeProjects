public class AmericanPan extends Pizza {

    @Override
    public String toString() {
        return "American Pan ";
    }
    @Override
    public int cost() {
        return 5 + this.MainPizzaTopping.cost();
    }

}
