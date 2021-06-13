public class Soudjouk extends PizzaTopping {
    private PizzaTopping PizzaTopping;

    public Soudjouk() {
        this.PizzaTopping = new NoTopping();
    }

    public Soudjouk(PizzaTopping pizzaTopping) {
        this.PizzaTopping = (pizzaTopping != null) ? pizzaTopping : new NoTopping();
    }

    @Override
    public String toString() {
        return this.PizzaTopping.toString() + "Soudjouk ";
    }

    @Override
    public int cost() {
        return this.PizzaTopping.cost() + 3;
    }
}
