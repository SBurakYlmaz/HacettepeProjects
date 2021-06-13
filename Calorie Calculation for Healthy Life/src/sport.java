import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class sport {
    int id;
    String name;
    int give_calories;
//reading the sport file and put the each line into an array
    public void reader_sport(sport ea_sport[]) {
        int i = 0;
        try {

            File f = new File("sport.txt");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();

            while (line != null) {
                sport sports = new sport();
                String[] parts = line.split("\t");
                sports.id = Integer.parseInt(parts[0]);
                sports.name = parts[1];
                sports.give_calories = Integer.parseInt(parts[2]);
                ea_sport[i] = sports;
                // System.out.println(line);
                i++;
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//checking the id of the given person and calculating the given calories

    public void sport_duration(int k, sport[] duration, int time, people[] human, int id,writer sport_writer) {
        int i, y;
        float result;
        for (i = 0; i < duration.length; i++) {
            if (human[i].id == k) {
                for (y = 0; y < duration.length; y++) {
                    if (id == duration[y].id) {
                        result = Math.round((time / 60.0) * duration[y].give_calories);
                        human[i].calories_burned = human[i].calories_burned + result;
                        String yaz =(human[i].id + "\t" + "has  burned" + "\t" + (int)result + "kcal thanks to" + "\t" + duration[y].name+"\n");
                        sport_writer.writer_sport(yaz);
                        break;
                    }
                }
                break;
            }
        }
    }
}