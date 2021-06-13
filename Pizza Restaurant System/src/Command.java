
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Command {

    public static void readercommand(String path) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<String> Ingredients=Arrays.asList("Neapolitan","AmericanPan","Onion","Salami","Soudjouk","HotPepper");
        String temps;String[] s;
        CustomerDaoImplementation customerdaoimp = new CustomerDaoImplementation("customer.txt");  //Creating customer.txt
        OrderDaoImplementation OrderDaoImplementation = new OrderDaoImplementation("order.txt");  //Creating order.txt
        OutputDaoImplementation output = new OutputDaoImplementation("output.txt");    //Creating output.txt
        File f = new File(path);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();

        while (line != null) {
            String[] temp_part = line.split(" ");

            //Controlling the command then apply the correct If/else statement

            if(temp_part[0].equals("AddCustomer")){
                s=Arrays.copyOfRange(temp_part,5,temp_part.length);
                temps=String.join(" ",s);
                Customer customer = new Customer(Integer.parseInt(temp_part[1]),temp_part[2],temp_part[3],temp_part[4],temps);
                customerdaoimp.addCustomer(customer);
                output.Addcustomer_writer(customer);
            }
            else if (temp_part[0].equalsIgnoreCase("CreateOrder")){
                output.Ordercreated_writer(OrderDaoImplementation.CreateOrder(Integer.parseInt(temp_part[1]),Integer.parseInt(temp_part[2])));
            }

            else if (temp_part[0].equalsIgnoreCase("PayCheck")){
                output.Paycheck_writer(Integer.parseInt(temp_part[1]), OrderDaoImplementation.Paycheck());//orders geliyo
            }

            else if (temp_part[0].equalsIgnoreCase("List")){
                output.Listofcustomers_writer(customerdaoimp.getCustomers());
            }

            else if (temp_part[0].equalsIgnoreCase("RemoveCustomer")){
                output.Removecustomer_writer(customerdaoimp.removeCustomer(Integer.parseInt(temp_part[1])));
                OrderDaoImplementation.Removeorder(Integer.parseInt(temp_part[1]));
            }

            else if (temp_part[0].equalsIgnoreCase("AddDrink")){
                OrderDaoImplementation.Addrink(Integer.parseInt(temp_part[1]));
                output.Drinkadded_writer(Integer.parseInt(temp_part[1]));
            }

            else if (temp_part[0].equalsIgnoreCase("AddPizza")) {
                boolean truth = true;
                ArrayList<String> templist = new ArrayList<>(Arrays.asList(temp_part));
                for (int counter = 2; counter < templist.size(); counter++) {
                    if (!(Ingredients.contains(templist.get(counter)))||templist.size() > 6) {//If more than 3 topping or invalid topping it gives error
                        truth = false;
                    }
                }
                if (truth) {
                    OrderDaoImplementation.AddPizza(Integer.parseInt(temp_part[1]), templist);
                    output.Pizzaadded_writer(Integer.parseInt(temp_part[1]), temp_part[2]);
                }
                else
                    output.Warningmessage();
                }
            line=br.readLine();
        }

        customerdaoimp.sorting_final_writer();
        OrderDaoImplementation.sorting_final_writer();
        br.close();
    }
}