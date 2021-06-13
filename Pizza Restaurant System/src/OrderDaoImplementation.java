
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class OrderDaoImplementation implements OrderDao {
    private File file;
    private List<OrderClass> orders;

    OrderDaoImplementation(String file){
        this.file = new File(file);
        if (!(this.file.length() == 0)) {   //If file has a content it deletes all content
            try {
                FileWriter fw = new FileWriter(this.file);
                fw.write("");
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        orders = new ArrayList<>();
    }

    @Override
    public void Addrink(Integer id) {   //Adding drink to correct order
        for (OrderClass orders : orders) {
            if (orders.getOrder_id().equals(id)) {
                SoftDrink softdrink = new SoftDrink();
                orders.getSoftDrinks().add(softdrink);
                break;
            }
        }
    }

    @Override
    public OrderClass CreateOrder(Integer order_id, Integer customer_id){ //Creating an order
        OrderClass order = new OrderClass(order_id, customer_id);
        orders.add(order);
        return order;
    }

    @Override
    public void AddPizza(Integer id, ArrayList<String> pizzamaker)throws ClassNotFoundException,InvocationTargetException,
            NoSuchMethodException,InstantiationException,IllegalAccessException{
        int count = pizzamaker.size();
        PizzaTopping topping = null;
        Pizza pizza ;

        for (OrderClass orders : orders) {     //Looking for OrderClass arrraylist and find the correct order and put the pizza toppings to order object
            if (orders.getOrder_id().equals(Integer.parseInt(pizzamaker.get(1)))) {
                pizza = (Pizza) Class.forName(pizzamaker.get(2)).getDeclaredConstructor().newInstance();
                orders.getPizzas().add(pizza);
                if (count != 3) {   //This if statement controlling if the pizza plane or not
                    for (int i = 3; i < pizzamaker.size(); i++) {
                        //For a given pizza adding topping to pizza.
                        if (topping == null)
                            pizza.addTopping(topping = (PizzaTopping) Class.forName(pizzamaker.get(i)).getDeclaredConstructor().newInstance());
                        else
                            pizza.addTopping(topping = (PizzaTopping) Class.forName(pizzamaker.get(i)).getDeclaredConstructor(PizzaTopping.class).newInstance(topping));
                    }
                    orders.getToppings().add(topping);
                    pizza.cost();
                } else {   //If the pizza is plane it continues with this else statement and making pizza with no toppings
                    pizza.addTopping(topping = (PizzaTopping) Class.forName("NoTopping").getDeclaredConstructor().newInstance());
                    orders.getToppings().add(null);

                }
            }
        }
    }

    @Override
    public List<OrderClass> Paycheck() {  //Returning the orders arraylist
        return orders;
    }

    @Override
    public void Removeorder(Integer id) {    //It removes the order looking the order id from order(arraylist)
        for (OrderClass order : orders) {
            if (order.getCustomer_id().equals(id)) {
                orders.remove(order);
                break;
            }
        }
    }

    public void sorting_final_writer() throws IOException {  //It sorts the order by looking order id
        FileWriter fw = new FileWriter(file);
        Collections.sort(orders, new Comparator<OrderClass>() {
            @Override
            public int compare(OrderClass o1, OrderClass o2) {
                return o1.getOrder_id().compareTo(o2.getOrder_id());
            }
        });
        for (OrderClass order : orders) {
            fw.write(String.format("Order: %d %d\n", order.getOrder_id(), order.getCustomer_id()));
            if (order.getPizzas().size() != 0) {   //This statement controls if the customer gave only drink or not if pizzas=0 it continues with below else statement
                for (int i = 0; i < order.getPizzas().size(); i++) {
                    if (order.getToppings().get(i) != null) {
                        fw.write(order.getPizzas().get(i).toString());
                        fw.write(order.getPizzas().get(i).printToppings());
                        fw.write("\n");
                    } else {
                        fw.write(order.getPizzas().get(i).toString());
                        fw.write("\n");
                    }
                    if (order.getSoftDrinks().size() != 0 && i == order.getPizzas().size() - 1) {
                        for (int k = 0; k < order.getSoftDrinks().size(); k++) {
                            fw.write(order.getSoftDrinks().get(k).toString());
                            fw.write("\n");
                        }
                    }
                }
            }
            else {
                for (int k = 0; k < order.getSoftDrinks().size(); k++) {
                    fw.write(order.getSoftDrinks().get(k).toString());
                    fw.write("\n");
                }
            }
        }
        fw.close();
    }
}





