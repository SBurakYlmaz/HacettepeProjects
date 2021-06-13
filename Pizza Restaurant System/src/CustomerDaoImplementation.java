import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CustomerDaoImplementation implements CustomerDao{
    private File file;
    private List<Customer> customers;

    CustomerDaoImplementation(String fileName) {

        file = new File(fileName);
        if (!(this.file.length() == 0)) {   //If file has a content it deletes all content
            try {
                FileWriter fw = new FileWriter(this.file);
                fw.write("");
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        customers = new ArrayList<>();
    }

    @Override
    public Customer addCustomer(Customer customer){  //Adding customer

        customers.add(customer);
        return customer;
    }

    @Override
    public Customer removeCustomer(Integer id){  //Controlling the id of the given customer then removing it

        Customer customerToRemove = null;

        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                customerToRemove = customer;
            }
        }
        if (customerToRemove != null) {
            customers.remove(customerToRemove);
        }

        return customerToRemove;
    }

    @Override
    public List<Customer> getCustomers(){
        return customers;
    }

    public void sorting_final_writer()throws IOException{
        FileWriter fw = new FileWriter(file);
        customers.sort(new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {     //Comparing objects by name and updating the arraylist
                return o1.getName().compareTo(o2.getName());
            }
        });
        for(Customer customer : customers){
            fw.write(String.format("%d %s %s %s\n",customer.getId(),customer.getName(),customer.getSurname(),customer.getNumber()));
        }
        fw.close();
    }
}
