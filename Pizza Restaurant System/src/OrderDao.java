import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public interface OrderDao {

    void Addrink(Integer id);
    OrderClass CreateOrder(Integer order_id, Integer customer_id);
    void AddPizza(Integer id,ArrayList<String> pizzamaker) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    List<OrderClass> Paycheck();
    void Removeorder(Integer id);
    void sorting_final_writer() throws IOException;
}
