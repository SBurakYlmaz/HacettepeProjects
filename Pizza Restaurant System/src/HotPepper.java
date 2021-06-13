public class HotPepper extends PizzaTopping {
    private PizzaTopping PizzaTopping;

    public HotPepper() {
        this.PizzaTopping = new NoTopping();
    }

    public HotPepper(PizzaTopping pizzaTopping) {
        this.PizzaTopping = pizzaTopping;
    }

    @Override
    public String toString() {
        return this.PizzaTopping.toString() + "HotPepper ";
    }

    @Override
    public int cost() {
        return this.PizzaTopping.cost() + 1;
    }
}