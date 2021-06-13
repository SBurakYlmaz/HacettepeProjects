import java.io.IOException;
import java.util.List;

public interface CustomerDao {

    Customer addCustomer(Customer customer);
    Customer removeCustomer(Integer id);
    List<Customer> getCustomers();
    void sorting_final_writer()throws IOException;
}