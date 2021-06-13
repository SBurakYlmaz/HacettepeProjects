public class Onion extends PizzaTopping {
    private PizzaTopping PizzaTopping;

    public Onion() {
        this.PizzaTopping = new NoTopping();
    }

    public Onion(PizzaTopping pizzaTopping) {
        this.PizzaTopping = pizzaTopping;
    }

    @Override
    public String toString() {
        return this.PizzaTopping.toString() + "Onion ";
    }

    @Override
    public int cost() {
        return this.PizzaTopping.cost() + 2;
    }
}
