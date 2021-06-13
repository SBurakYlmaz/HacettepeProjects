import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public class command {

    //reading the command.txt


    public void cm_reader(String path, people[] human, food[] meal, sport[] ea_sport, writer write) {
        int i, j;
        double total;
        int temp1 = 0;
        int temp2;
        Integer[] ids = new Integer[100];
        char compare;
        String str;
        try {
            File f = new File(path);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                if (line.toLowerCase().contains("print(")) {
                    line = line.replaceAll("\\(", " ");
                    line = line.replaceAll("\\)", " ");
                    String[] temp_part = line.split(" ");
                    int temp = Integer.parseInt(temp_part[1]);
                    for (i = 0; i < human.length; i++) {
                        if (temp == human[i].id) {
                            total = (int)human[i].required_calories - (int)human[i].calories_taken + (int)human[i].calories_burned;
                            total = (-total);
                            //total = ;
                            if(total>0)
                                str ="+"+Integer.toString((int)Math.round(total));
                            else {
                                str = Integer.toString((int)Math.round(total));
                            }
                            String yaz = (human[i].name + "\t" + human[i].age + "\t" + (int) Math.round(human[i].required_calories) + "kcal" +
                                    "\t" +(int) human[i].calories_taken + "kcal" + "\t" +
                                    (int) human[i].calories_burned + "kcal" + "\t" + str + "kcal" + "\n");
                            write.writer_people(yaz);
                            break;
                        }
                    }
                } else if (line.toLowerCase().contains("print")) {
                        for (i = 0; i < ids.length; i++) {
                            for (j = 0; j < human.length; j++) {
                                if (ids[i] != null && ids[i] == human[j].id ) {
                                    people person = human[j];
                                    write.writer_object(person);
                                    break;
                                }
                            }
                        }
                } else {
                    String[] temp_part = line.split("\t");
                    temp2 = Integer.parseInt(temp_part[0]);
                    if (!(Arrays.asList(ids).contains(temp2))) {
                        ids[temp1] = Integer.parseInt(temp_part[0]);
                        temp1++;
                    }
                    compare = temp_part[1].charAt(0);
                    if ('1' == compare) {
                        food eat = new food();
                        eat.portion_calories(Integer.parseInt(temp_part[0]), meal, Integer.parseInt(temp_part[2]), human, Integer.parseInt(temp_part[1]), write);
                    } else {
                        sport sports = new sport();
                        sports.sport_duration(Integer.parseInt(temp_part[0]), ea_sport, Integer.parseInt(temp_part[2]), human, Integer.parseInt(temp_part[1]), write);
                    }
                }
                line = br.readLine();
                if (line != null)
                    write.writer_stars();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
