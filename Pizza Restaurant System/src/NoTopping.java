public class NoTopping extends PizzaTopping {

    @Override
    public String toString() {
        return "";
    }

    @Override
    public int cost() {
        return 0;
    }
}
