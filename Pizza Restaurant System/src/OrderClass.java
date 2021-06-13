
import java.util.ArrayList;
import java.util.List;

public class OrderClass {
    private Integer order_id;
    private Integer customer_id;
    private ArrayList<Pizza> pizzas;
    private ArrayList<PizzaTopping> toppings;
    private ArrayList<SoftDrink> softDrinks;


    public OrderClass(Integer order_id, Integer customer_id) {
        this.order_id = order_id;
        this.customer_id = customer_id;
        pizzas=new ArrayList<>();
        toppings = new ArrayList<>();
        softDrinks = new ArrayList<>();
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public ArrayList<PizzaTopping> getToppings() {
        return toppings;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public List<SoftDrink> getSoftDrinks() {
        return softDrinks;
    }

}
