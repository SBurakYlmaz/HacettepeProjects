import java.io.File;
import java.io.FileWriter;

public class writer {
    String path;

//taking the output file name
    public writer(String path){
        this.path=path;
    }

//It checks the file if it's not empty clear the file
    public void check_empty(){
        File f = new File(path);
        if(!(f.length()==0)){
            try {
                FileWriter monitor_writer = new FileWriter(f);
                monitor_writer.write("");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
//Writing the correct output of the sports class
    public void writer_sport(String s){

       try {
           File f=new File(path);
               FileWriter monitor_writer = new FileWriter(f, true);
               monitor_writer.write(s);
               monitor_writer.close();

       }
        catch (Exception e){
           e.printStackTrace();
        }
    }
//Writing the correct output of the food class

    public void writer_food(String s){
        try {
            File f = new File(path);
                FileWriter monitor_writer = new FileWriter(f, true);
                monitor_writer.write(s);
                monitor_writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Writing the correct output of the people class

    public void writer_people(String s){
        try {
            File f=new File(path);
                FileWriter monitor_writer = new FileWriter(f, true);
                monitor_writer.write(s);
                monitor_writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    //Writing stars

    public void writer_stars(){
        try {
            File f = new File(path);
                FileWriter monitor_writer = new FileWriter(f, true);
                monitor_writer.write("***************\n");
                monitor_writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Writing the correct output of the object class

    public void writer_object(people printer){
        try {
            File f = new File(path);
                FileWriter monitor_writer = new FileWriter(f, true);
                monitor_writer.write(printer.toString());
                monitor_writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

