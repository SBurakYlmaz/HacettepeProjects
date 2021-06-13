import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class OutputDaoImplementation implements OutputDao {
    private File file;

    public OutputDaoImplementation(String path) {    //If file has a content it deletes all content
        this.file = new File(path);
        if (!(this.file.length() == 0)) {
            try {
                FileWriter monitor_writer = new FileWriter(this.file);
                monitor_writer.write("");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void Addcustomer_writer(Customer customer) throws IOException {   //Writing output.txt to added customer
        FileWriter fw = new FileWriter(file,true);
        fw.write(String.format("Customer %d %s added\n",customer.getId(),customer.getName()));
        fw.close();
    }

    @Override
    public void Ordercreated_writer(OrderClass order) throws IOException {   //Writing output.txt to order created
        FileWriter fw = new FileWriter(file,true);
        fw.write(String.format("Order %d created\n",order.getOrder_id()));
        fw.close();
    }

    @Override
    public void Drinkadded_writer(Integer id) throws IOException {  //Writing output.txt to SoftDrink added
        FileWriter fw = new FileWriter(file,true);
        fw.write(String.format("SoftDrink added to order %d\n",id));
        fw.close();
    }

    @Override
    public void Paycheck_writer(Integer id, List<OrderClass> lister) throws IOException {  //Writing output.txt to total bill
        FileWriter fw = new FileWriter(file,true);
        double total =0;
        for (OrderClass order : lister) {
            if (order.getOrder_id().equals(id)){
                fw.write(String.format("Paycheck for order %d\n",order.getOrder_id()));
                for(int i =0;i<order.getPizzas().size();i++){
                    fw.write(String.format("\t%s",order.getPizzas().get(i).toString()));
                    if(order.getToppings().get(i)!=null){
                        total = total+order.getPizzas().get(i).cost();
                        fw.write(String.format("%s",order.getPizzas().get(i).printToppings()));
                        fw.write(String.valueOf(order.getPizzas().get(i).cost()));   //Calculating the cost of the pizza
                        fw.write("$\n");
                    }
                    else {
                        total = total+order.getPizzas().get(i).cost();
                        fw.write(String.valueOf(order.getPizzas().get(i).cost()));
                        fw.write("$\n");
                    }

                }
                for(int i = 0; i<order.getSoftDrinks().size(); i++){   //Same operations for drinks
                    total=total+order.getSoftDrinks().get(i).getCost();
                    fw.write(String.format("\t%s",order.getSoftDrinks().get(i).toString()));
                    fw.write(String.format("%d",(int)order.getSoftDrinks().get(i).getCost()));
                    fw.write("$\n");
                }

                fw.write(String.format("\tTotal: %d$\n",(int)total));
            }
        }
        fw.close();
    }


    @Override
    public void Listofcustomers_writer(List<Customer> customers) throws IOException {  //Writing output.txt to all customers
        FileWriter fw = new FileWriter(file,true);
        fw.write("Customer List:\n");
        customers.sort(new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {     //Comparing objects by name and updating the arraylist
                return o1.getName().compareTo(o2.getName());
            }
        });
        for(Customer customer: customers){
            fw.write(String.format("%d %s %s %s \n", customer.getId(), customer.getName(), customer.getSurname(),
                    customer.getNumber()));
        }
        fw.close();
    }

    @Override
    public void Pizzaadded_writer(Integer id,String pizzaname) throws IOException {  //Writing output.txt to pizza added
        FileWriter fw = new FileWriter(file, true);
        fw.write(String.format("%s pizza added to order %d\n",pizzaname,id));
        fw.close();
    }

    @Override
    public void Removecustomer_writer(Customer customer) throws IOException {  //Writing output.txt to removed customer
        if (customer==null){
            return;
        }
        FileWriter fw = new FileWriter(file,true);
        fw.write(String.format("Customer %d %s removed\n",customer.getId(),customer.getSurname()));
        fw.close();
    }

    @Override
    public void Warningmessage() throws IOException {   //Writing output.txt to invalid topping or more than 3 topping
        FileWriter fw = new FileWriter(file, true);
        fw.write("WARNING: You entered more than three toppings or unexpected topping,This order invalid\n");
        fw.close();
    }
}
