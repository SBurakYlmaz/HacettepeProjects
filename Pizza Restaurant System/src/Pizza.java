public abstract class Pizza {

    protected PizzaTopping MainPizzaTopping;

    public void addTopping(PizzaTopping pizzaToppingDecorator) {
        this.MainPizzaTopping = pizzaToppingDecorator;
    }


    public String printToppings() {
        return MainPizzaTopping.toString();
    }

    public abstract int cost();
}
