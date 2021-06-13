public class Salami extends PizzaTopping {
    private PizzaTopping PizzaTopping;

    public Salami() {
        this.PizzaTopping = new NoTopping();
    }

    public Salami(PizzaTopping pizzaTopping) {
        this.PizzaTopping = pizzaTopping;
    }

    @Override
    public String toString() {
        return this.PizzaTopping.toString() + "Salami ";
    }

    @Override
    public int cost() {
        return this.PizzaTopping.cost() + 3;
    }
}
