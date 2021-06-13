import java.io.*;
import java.util.ArrayList;
import java.util.Objects;


public class Read_File {
    private  ArrayList<Binary_tree> population= new ArrayList<>();
    private  ArrayList<Binary_tree> area_total= new ArrayList<>();
    private  ArrayList<Binary_tree> area_land= new ArrayList<>();
    private  ArrayList<Binary_tree> area_water= new ArrayList<>();
    private  ArrayList<Binary_tree> median_age_male= new ArrayList<>();
    private  ArrayList<Binary_tree> median_Age_female=new ArrayList<>();
    private  ArrayList<Binary_tree> birth_rate=new ArrayList<>();
    private  ArrayList<Binary_tree> death_rate= new ArrayList<>();
    private  ArrayList<Binary_tree> literacy_female= new ArrayList<>();
    private  ArrayList<Binary_tree> airports= new ArrayList<>();
    private final int[] right = new int[65536];
    private Binary_tree root_population=null;
    private Binary_tree root_area_total=null;
    private Binary_tree root_area_land=null;
    private Binary_tree root_area_water=null;
    private Binary_tree root_median_age_male=null;
    private Binary_tree root_median_age_female=null;
    private Binary_tree root_birth_rate=null;
    private Binary_tree root_death_rate=null;
    private Binary_tree root_literacy_female=null;
    private Binary_tree root_airports=null;




    private final String[] pattern_words=new String[]{"\"Area\"", "\"Population\"","\"Median age\"","\"Birth rate\"","\"Death rate\"",
            "\"Literacy\"","\"Country name\"","\"Airports\""};

    /*For boyer-moore bad suffix implementation I stored an array and fill at the beginning with -1 as considering unicode characters*/
    public Read_File() {
        for (int i= 0; i < 65536; i++)
            right[i] = -1;
    }

    public ArrayList<Binary_tree> getPopulation() {
        return population;
    }

    public ArrayList<Binary_tree> getArea_total() {
        return area_total;
    }

    public ArrayList<Binary_tree> getArea_land() {
        return area_land;
    }

    public ArrayList<Binary_tree> getArea_water() {
        return area_water;
    }

    public ArrayList<Binary_tree> getMedian_age_male() {
        return median_age_male;
    }

    public ArrayList<Binary_tree> getMedian_Age_female() {
        return median_Age_female;
    }

    public ArrayList<Binary_tree> getBirth_rate() {
        return birth_rate;
    }

    public ArrayList<Binary_tree> getDeath_rate() {
        return death_rate;
    }

    public ArrayList<Binary_tree> getLiteracy_female() {
        return literacy_female;
    }

    public ArrayList<Binary_tree> getAirports() {
        return airports;
    }

    public void Reading_Entities(File[] list_of_files) throws IOException{

        /*For each folder take the whole files one by one and search the given strings inside*/

        for(File txt_files:list_of_files){
            for(File read_txt: Objects.requireNonNull(txt_files.listFiles()))
                search(read_txt,pattern_words);

        }
        /*Creating 10 binary-search-trees*/
        for(int i=0;i<10;i++){
            switch (i){
                case 0:
                    BST_to_Sorted_Arraylist(root_area_total,area_total);
                    break;
                case 1:
                    BST_to_Sorted_Arraylist(root_area_land,area_land);
                    break;
                case 2:
                    BST_to_Sorted_Arraylist(root_area_water,area_water);
                    break;
                case 3:
                    BST_to_Sorted_Arraylist(root_population,population);
                    break;
                case 4:
                    BST_to_Sorted_Arraylist(root_median_age_male,median_age_male);
                    break;
                case 5:
                    BST_to_Sorted_Arraylist(root_median_age_female,median_Age_female);
                    break;
                case 6:
                    BST_to_Sorted_Arraylist(root_birth_rate,birth_rate);
                    break;
                case 7:
                    BST_to_Sorted_Arraylist(root_death_rate,death_rate);
                    break;
                case 8:
                    BST_to_Sorted_Arraylist(root_literacy_female,literacy_female);
                    break;
                case 9:
                    BST_to_Sorted_Arraylist(root_airports,airports);
                    break;
            }
        }
    }

    public ArrayList<String> split(char[] queryString, char split) {

        ArrayList<String> result = new ArrayList<>();

        int index;
        /*It splits the given query with the given split parameter.Index is the delimiter index*/
        while((index = Boyer_Moore_Search(queryString, new char[]{split})) != -1) {

            /*Before array stores the characters of the given query before the delimiter*/
            char[] before = new char[index];
            int i = 0;
            while (i < index) {
                before[i] = queryString[i];
                i++;
            }

            int len = queryString.length;

            /*After array stores the characters of the given query after the delimiter*/

            char[] after = new char[len - index - 1];
            if (len - 1 - index >= 0) System.arraycopy(queryString, index + 1, after, 0, len - 1 - index);

            queryString = after;

            result.add(new String(before));
        }

        /*If the query does not contains the delimiter take the whole query there is nothing to split*/
        result.add(new String(queryString));

        return result;
    }

    public void search(File txt,String[] searched_string) throws IOException{
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(txt)));
        Countries new_country= new Countries();
        String str=": ";
        char[] split_array= str.toCharArray();
        int word_order=0;
        int counter=0;
        String line =reader.readLine();

        /*Read until the end of file occurs*/
        while (line!=null){
            int k=-1;
            for(int t=0;t<searched_string.length;t++){
                k=Boyer_Moore_Search(line.toCharArray(),searched_string[t].toCharArray());

                /*If k!=-1 it means our algorithm find the requested substring at that line*/

                if(k!=-1){
                    word_order=t;/*Word_order stores which string found*/
                    break;
                }
            }
            if(k!=-1){
                if(word_order==0){ /*Taking the values of area and set the values*/
                    while (counter!=3){
                        if(Boyer_Moore_Search(line.toCharArray(),"text".toCharArray())==-1)
                            line=reader.readLine();
                        else {

                            /*Deciding each value start and end indexes */
                            int index =Boyer_Moore_Search(line.toCharArray(),split_array)+1+split_array.length;
                            int last= get_last_index(line.toCharArray(),index,0);
                            set_result(new_country,line.toCharArray(),index,last,counter++);
                            line=reader.readLine();
                        }
                    }
                }
                else if (word_order==1){ /*Taking the values of population and set the values*/
                    counter=3;
                    while (counter!=4) {
                        if (Boyer_Moore_Search(line.toCharArray(), "text".toCharArray()) == -1)
                            line = reader.readLine();
                        else {

                            /*Deciding each value start and end indexes */
                            int index =Boyer_Moore_Search(line.toCharArray(),split_array)+split_array.length+1;
                            int last= get_last_index(line.toCharArray(),index,1);
                            set_result(new_country,line.toCharArray(),index,last,counter++);
                            line=reader.readLine();
                        }
                    }
                }
                else if(word_order==2){ /*Taking the values of median-age and set the values*/
                    counter=4;
                    boolean total=true;
                    while (counter!=6) {
                        if (Boyer_Moore_Search(line.toCharArray(), "text".toCharArray()) == -1)
                            line = reader.readLine();
                        else if(total){
                            total=false;
                            line = reader.readLine();
                        }
                        else {

                            /*Deciding each value start and end indexes */
                            int index =Boyer_Moore_Search(line.toCharArray(),split_array)+split_array.length+1;
                            int last= get_last_index(line.toCharArray(),index,2);
                            set_result(new_country,line.toCharArray(),index,last,counter++);
                            line=reader.readLine();
                        }
                    }
                }
                else if(word_order==3){/*Taking the values of birth_rate and set the values*/
                    counter=6;
                    while (counter!=7) {
                        if (Boyer_Moore_Search(line.toCharArray(), "text".toCharArray()) == -1)
                            line = reader.readLine();
                        else {

                            /*Deciding each value start and end indexes */
                            int index =Boyer_Moore_Search(line.toCharArray(),split_array)+split_array.length+1;
                            int last= get_last_index(line.toCharArray(),index,3);
                            set_result(new_country,line.toCharArray(),index,last,counter++);
                            line=reader.readLine();
                        }
                    }
                }
                else if(word_order==4){/*Taking the values of death_rate and set the values*/
                    counter=7;
                    while (counter!=8) {
                        if (Boyer_Moore_Search(line.toCharArray(), "text".toCharArray()) == -1)
                            line = reader.readLine();
                        else {

                            /*Deciding each value start and end indexes */
                            int index =Boyer_Moore_Search(line.toCharArray(),split_array)+split_array.length+1;
                            int last= get_last_index(line.toCharArray(),index,4);
                            set_result(new_country,line.toCharArray(),index,last,counter++);
                            line=reader.readLine();
                        }
                    }
                }
                else if(word_order==5){/*Taking the values of literacy-female and set the values*/
                    counter=8;
                    while (counter!=9) {
                        if (Boyer_Moore_Search(line.toCharArray(), "female".toCharArray()) == -1) {
                            line = reader.readLine();
                        }
                        else {
                            while(Boyer_Moore_Search(line.toCharArray(), "text".toCharArray()) == -1){
                                line = reader.readLine();
                            }

                            /*Deciding each value start and end indexes */
                            int index =Boyer_Moore_Search(line.toCharArray(),split_array)+1+split_array.length;
                            int last= get_last_index(line.toCharArray(),index,5);
                            set_result(new_country,line.toCharArray(),index,last,counter++);
                            line=reader.readLine();
                        }
                    }
                }
                else if(word_order==6){/*Taking the values of country name and set the values*/
                    counter=9;
                    while (counter!=10) {
                        if (Boyer_Moore_Search(line.toCharArray(), "conventional short form".toCharArray()) == -1) {
                            line = reader.readLine();
                        }
                        else {
                            while(Boyer_Moore_Search(line.toCharArray(), "text".toCharArray()) == -1){
                                line = reader.readLine();
                            }

                            /*Deciding each value start and end indexes */
                            int index =1+Boyer_Moore_Search(line.toCharArray(),split_array)+split_array.length;
                            int last= get_last_index(line.toCharArray(),index,6);
                            set_result(new_country,line.toCharArray(),index,last,counter++);
                            line=reader.readLine();
                        }
                    }
                }
                else if(word_order==7){/*Taking the values of airports and set the values*/
                    counter=10;
                    while (counter!=11) {
                        if (Boyer_Moore_Search(line.toCharArray(), "text".toCharArray()) == -1)
                            line = reader.readLine();
                        else {

                            /*Deciding each value start and end indexes */
                            int index =Boyer_Moore_Search(line.toCharArray(),split_array)+split_array.length+1;
                            int last= get_last_index(line.toCharArray(),index,7);
                            set_result(new_country,line.toCharArray(),index,last,counter++);
                            line=reader.readLine();
                        }
                    }
                }
            }
            line=reader.readLine();
        }

        Binary_tree node;
        /*Creating binary trees for the each given category*/
        for(int i=0;i<10;i++){
            switch (i){
                case 0:
                    node = new Binary_tree(new_country.getArea_total(),new_country.getCountry_name());
                    if(root_area_total==null)
                        root_area_total = new Binary_tree(node);
                    else if(new_country.getArea_total()!=null)
                        root_area_total= root_area_total.insert(root_area_total,node);
                    break;
                case 1:
                    node = new Binary_tree(new_country.getArea_land(),new_country.getCountry_name());
                    if(root_area_land==null)
                        root_area_land=new Binary_tree(node);
                    else if(new_country.getArea_land()!=null)
                        root_area_land=root_area_land.insert(root_area_land,node);
                    break;
                case 2:
                    node = new Binary_tree(new_country.getArea_water(),new_country.getCountry_name());
                    if(root_area_water==null)
                        root_area_water=new Binary_tree(node);
                    else if(new_country.getArea_water()!=null)
                        root_area_water=root_area_water.insert(root_area_water,node);
                    break;
                case 3:
                    node = new Binary_tree(new_country.getPopulation(),new_country.getCountry_name());
                    if(root_population==null)
                        root_population=new Binary_tree(node);
                    else if(new_country.getPopulation()!=null)
                        root_population=root_population.insert(root_population,node);
                    break;
                case 4:
                    node = new Binary_tree(new_country.getMedian_age_male(),new_country.getCountry_name());
                    if(root_median_age_male==null)
                        root_median_age_male=new Binary_tree(node);
                    else if(new_country.getMedian_age_male()!=null)
                        root_median_age_male=root_median_age_male.insert(root_median_age_male,node);
                    break;
                case 5:
                    node = new Binary_tree(new_country.getMedian_age_female(),new_country.getCountry_name());
                    if(root_median_age_female==null)
                        root_median_age_female=new Binary_tree(node);
                    else if(new_country.getMedian_age_female()!=null)
                        root_median_age_female=root_median_age_female.insert(root_median_age_female,node);
                    break;
                case 6:
                    node = new Binary_tree(new_country.getBirth_rate(),new_country.getCountry_name());
                    if(root_birth_rate==null)
                        root_birth_rate=new Binary_tree(node);
                    else if(new_country.getBirth_rate()!=null)
                        root_birth_rate=root_birth_rate.insert(root_birth_rate,node);
                    break;
                case 7:
                    node = new Binary_tree(new_country.getDeath_rate(),new_country.getCountry_name());
                    if(root_death_rate==null)
                        root_death_rate=new Binary_tree(node);
                    else if(new_country.getDeath_rate()!=null)
                        root_death_rate=root_death_rate.insert(root_death_rate,node);
                    break;
                case 8:
                    node = new Binary_tree(new_country.getLiteracy_female(),new_country.getCountry_name());
                    if(root_literacy_female==null)
                        root_literacy_female=new Binary_tree(node);
                    else if(new_country.getLiteracy_female()!=null)
                        root_literacy_female=root_literacy_female.insert(root_literacy_female,node);
                    break;
                case 9:
                    node = new Binary_tree(new_country.getAirports(),new_country.getCountry_name());
                    if(root_airports==null)
                        root_airports=new Binary_tree(node);
                    else if(new_country.getAirports()!=null)
                        root_airports=root_airports.insert(root_airports,node);
                    break;
                default:
            }
        }
    }

    /*Boyer Moore search algorithm with bad suffix implementation*/

    public int Boyer_Moore_Search(char[] line,char[] pattern){
        for (int j = 0; j < pattern.length; j++){
            right[pattern[j]] = j;
        }
        int searched_word_length = pattern.length;
        int line_length = line.length;
        int skip;
        for (int i = 0; i <= line_length - searched_word_length; i += skip) {
            skip = 0;
            for (int j = searched_word_length-1; j >= 0; j--) {
                if (pattern[j] != line[i+j]) {
                    skip = Math.max(1, j - right[line[i+j]]); /*Decides how many characters is gonna be skip*/
                    break;
                }
            }
            if (skip == 0) {
                for (char c : pattern) {
                    right[c] = -1;
                }
                return i;
            }
        }
        for (char c : pattern) {
            right[c] = -1;
        }
        return -1;/*If the given string is not in the line that we are looking for simply returns -1*/
    }

    /*Getting the last index of the substring*/
    public int get_last_index(char[] line,int start,int offset){
        int i=start;
        if(offset==6){/*This is for only the country name because some countries names consists of two seperate strings*/
            while(line[i]!= '"'){
                start++;
                i++;
            }
        }
        else {
            while(line[i]!=' ' && line[i]!= '"'){
                start++;
                i++;
            }
        }

        return start;
    }
    public void set_result(Countries country,char[] line,int start,int last,int order) {
        Float float_value=(float)0;
        int million_index=Boyer_Moore_Search(line,"million".toCharArray());
        StringBuilder result=new StringBuilder();
        for(int i =start;i<last;i++){
            if(line[i]==',' || line[i]=='%'){
                continue;
            }
            result.append(line[i]);
        }
        /*For undefined data */
        if(Boyer_Moore_Search(result.toString().toCharArray(),"NA".toCharArray())!=-1)
            return;

        /*Some of the data includes million to handle that problem I used below implementation*/
        if(million_index!=-1){
            if(result.toString().length()==last-start){
                int decimal_part=Boyer_Moore_Search(result.toString().toCharArray(),".".toCharArray());

                /*This means the number is only consists of a base part like 1 million or 23 million*/
                if(decimal_part==-1){
                    for(int i=0;i<6;i++)
                        result.append("0");
                    result.append(".");
                    result.append("0");
                }
                /*That means we have fraction like 22.235 million so to handle this store the decimal part and the base part differently
                * and at the end add them to get the actual result of the data*/
                else {
                    char[] array=result.toString().toCharArray();
                    StringBuilder decimal = new StringBuilder();
                    for(int i=decimal_part+1;i<last-start;i++){
                        decimal.append(array[i]);
                    }
                    int offset=decimal.toString().length();
                    for(int i =0;i<6-offset;i++){
                        decimal.append("0");
                    }
                    StringBuilder actual_part = new StringBuilder();
                    for(int k=0;k<decimal_part;k++){
                        actual_part.append(array[k]);
                    }
                    for(int i=0;i<6;i++)
                        actual_part.append("0");
                    Integer i1=Integer.parseInt(actual_part.toString());
                    Integer i2=Integer.parseInt(decimal.toString());
                    Integer result1 =i2+i1;
                    result.setLength(0);
                    result.append(result1);

                }
            }
        }
        if(order!=9) { /*If it is the country name do not convert it to float*/
            float_value=Float.parseFloat(result.toString());
        }
        /*For the given order value ıt decides which part of the object is gonna be updated*/
        if(order==0)
            country.setArea_total(float_value);
        else if(order==1)
            country.setArea_land(float_value);
        else if(order==2)
            country.setArea_water(float_value);
        else if(order==3)
            country.setPopulation(float_value);
        else if(order==4)
            country.setMedian_age_male(float_value);
        else if(order==5)
            country.setMedian_age_female(float_value);
        else if(order==6)
            country.setBirth_rate(float_value);
        else if(order==7)
            country.setDeath_rate(float_value);
        else if(order==8)
            country.setLiteracy_female(float_value);
        else if(order==9)
            country.setCountry_name(result.toString());
        else if(order==10)
            country.setAirports(float_value);
    }
    /*Basically it adds elements of the current  tree to an array-list as considering the inorder traversal so the given array-list
    * will be sorted*/
    public void BST_to_Sorted_Arraylist(Binary_tree node,ArrayList<Binary_tree> arrayList){
        if (node == null)
            return;
        BST_to_Sorted_Arraylist(node.getLeft_link(),arrayList);
        arrayList.add(node);
        BST_to_Sorted_Arraylist(node.getRight_link(),arrayList);
    }
/*    public void File_writer(FileWriter fileWriter,ArrayList<Binary_tree> arrayList) throws IOException {
        for(Binary_tree node:arrayList){
            fileWriter.write(String.format("%s  %.1f\n",node.getCountry_name(),node.getValue()));
        }
    }*/

}
