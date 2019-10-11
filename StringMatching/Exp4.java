import java.io.*;
import java.util.ArrayList;
import java.util.Locale;


public class Exp4 {
    public static void main(String[] args) throws IOException{

        /*Checks the arguments of the program if less more than expected it prints an error and exits*/
        if(args.length != 3)
        {
            System.out.println("Not enough or too many arguments");
            System.exit(0);
        }

        Read_File read_file = new Read_File();
        ArrayList<String> params;
        ArrayList<String> split ;
        FileWriter fileWriter = new FileWriter(args[2]);

        Locale.setDefault(Locale.GERMAN);
        Read_File file_read = new Read_File();
        File folder = new File(args[0]);

        if(!folder.exists()){
            System.out.println("The file you try to read is not exist");
            System.exit(1);
        }

        File[] list_of_files = folder.listFiles();
        assert list_of_files != null;
        if(list_of_files.length==0){
            System.out.println("This folder is empty");
            System.exit(2);
        }
        file_read.Reading_Entities(list_of_files);

        folder=new File(args[1]);
        BufferedReader reader= new BufferedReader(new InputStreamReader(new FileInputStream(folder)));
        String line=reader.readLine();

        while (line!=null){
            String_sort root=new String_sort();

            /*It splits the line with given delimiter and put the values into an array-list
            * Split function written by me.Not a ready function or a library function */
            split = read_file.split(line.toCharArray(), '+');

            for (String sp : split) {
                params = read_file.split(sp.toCharArray(), ',');

                ArrayList<Binary_tree> root_current = null;

                // applying commands.It looks the pattern in line and chooses correct binary-tree to perform operations

                if (file_read.Boyer_Moore_Search(params.get(0).toCharArray(), "population".toCharArray()) != -1) {
                    root_current = file_read.getPopulation();
                } else if (file_read.Boyer_Moore_Search(params.get(0).toCharArray(), "area-total".toCharArray()) != -1) {
                    root_current = file_read.getArea_total();
                }else if (file_read.Boyer_Moore_Search(params.get(0).toCharArray(), "area-land".toCharArray()) != -1) {
                    root_current = file_read.getArea_land();
                }else if (file_read.Boyer_Moore_Search(params.get(0).toCharArray(), "area-water".toCharArray()) != -1) {
                    root_current = file_read.getArea_water();
                }else if (file_read.Boyer_Moore_Search(params.get(0).toCharArray(), "median_age-female".toCharArray()) != -1) {
                    root_current = file_read.getMedian_Age_female();
                }else if (file_read.Boyer_Moore_Search(params.get(0).toCharArray(), "median_age-male".toCharArray()) != -1) {
                    root_current = file_read.getMedian_age_male();
                }else if (file_read.Boyer_Moore_Search(params.get(0).toCharArray(), "death_rate".toCharArray()) != -1) {
                    root_current = file_read.getDeath_rate();
                }else if (file_read.Boyer_Moore_Search(params.get(0).toCharArray(), "airports".toCharArray()) != -1) {
                    root_current = file_read.getAirports();
                }else if (file_read.Boyer_Moore_Search(params.get(0).toCharArray(), "birth_rate".toCharArray()) != -1) {
                    root_current = file_read.getBirth_rate();
                }else if (file_read.Boyer_Moore_Search(params.get(0).toCharArray(), "literacy-female".toCharArray()) != -1) {
                    root_current = file_read.getLiteracy_female();
                }

                /*It checks if the given category(10 category we have)is requested.For example if it asks like
                * railway stations it gives an error and continues with an input*/

                if (root_current != null) {

                    int count = Integer.parseInt(params.get(2));

                    /*If the given line contains top it inserts tree the top elements of that category*/
                    if (file_read.Boyer_Moore_Search(params.get(1).toCharArray(), "top".toCharArray()) != -1) {
                        for (int i = 0; i < count; i++) {
                            root=root.insert(root,new String_sort(root_current.get(i).getCountry_name()));
                        }

                        /*If the given line contains last it inserts tree the last elements of that category*/

                    } else if (file_read.Boyer_Moore_Search(params.get(1).toCharArray(), "last".toCharArray()) != -1) {
                        for (int i = root_current.size() - 1; i > root_current.size() - count - 1; i--) {
                            root=root.insert(root,new String_sort(root_current.get(i).getCountry_name()));
                        }
                        /*If the other than top or last like average it gives error*/
                    } else {
                        throw new IllegalArgumentException("sort parameter is not invalid: " + params);
                    }

                }
            }
            if(root.getCountry_name()!=null) {
                fileWriter.write("[");
                root.Tree_Sort_Write_File(root, fileWriter);
                fileWriter.write("]\n");
            }
            else
                fileWriter.write("Invalid category\n");
            line=reader.readLine();
        }
        fileWriter.close();
    }
}
