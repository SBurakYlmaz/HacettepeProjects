import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Objects;

public class people {
    int id;
    String name;
    String gender;
    int weight;
    int height;
    int age;
    double required_calories;
    float calories_taken;
    float calories_burned;
//reading the people file and put the each line into an array

    public void reader_people(people human[]) {
        int i = 0;
        try {
            File f = new File("people.txt");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();

            while (line != null) {
                String[] parts = line.split("\t");
                people peoples = new people();
                peoples.id = Integer.parseInt(parts[0]);
                peoples.name = parts[1];
                peoples.gender = parts[2];
                peoples.weight = Integer.parseInt(parts[3]);
                peoples.height = Integer.parseInt(parts[4]);
                peoples.age = 2018 - Integer.parseInt(parts[5]);
                if (parts[2].equals("male"))
                    peoples.required_calories = Math.round((13.75 * Integer.parseInt(parts[3])) + (5 * Integer.parseInt(parts[4])) - (6.8 * peoples.age) + 66);

                else
                    peoples.required_calories = Math.round((9.6 * Integer.parseInt(parts[3])) + (1.7 * Integer.parseInt(parts[4])) - (4.7 * peoples.age) + 665);
                human[i] = peoples;
                line = br.readLine();
                i++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        String str;
        int total = (int) Math.round(this.required_calories + this.calories_burned - this.calories_taken);
        total = -total;
        if(total>0)
            str ="+"+Integer.toString(Math.round(total));
        else {
            str = Integer.toString(Math.round(total));
        }
        return (this.name + "\t" + this.age + "\t" + (int) this.required_calories + "kcal" + "\t" + (int) this.calories_taken + "kcal" + "\t" + (int) this.calories_burned + "kcal" +
                "\t" +str + "kcal\n");
    }
}
