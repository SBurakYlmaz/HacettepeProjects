
import java.io.IOException;
import java.util.List;

public interface OutputDao {
    void Addcustomer_writer(Customer customer) throws IOException;
    void Ordercreated_writer(OrderClass order) throws IOException;
    void Drinkadded_writer(Integer id) throws IOException;
    void Paycheck_writer(Integer id, List<OrderClass> lister) throws IOException;
    void Listofcustomers_writer(List<Customer> customers) throws IOException;
    void Pizzaadded_writer(Integer id,String pizzaname) throws IOException;
    void Removecustomer_writer(Customer customer) throws IOException;
    void Warningmessage()throws IOException;
}
