import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class food {
    int id;
    String name;
    int gain_calories;
//reading the food file and put the each line into an array

    public void reader_food(food meal[]) {
        int i = 0;
        try {

            File f = new File("food.txt");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();

            while (line != null) {
                String[] parts = line.split("\t");
                food foods = new food();
                foods.id = Integer.parseInt(parts[0]);
                foods.name = parts[1];
                foods.gain_calories = Integer.parseInt(parts[2]);
                meal[i] = foods;
                i++;
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //checking the id of the given person and calculating the porsion

    public void portion_calories(int k, food[] porsion, int eaten_porsion, people[] human, int id, writer food_writer) {
        int i, y;
        int result;
        for (i = 0; i < human.length; i++) {
            if (k == human[i].id) {
                for (y = 0; y < porsion.length; y++) {
                    if (id == porsion[y].id) {
                        result = eaten_porsion * porsion[y].gain_calories;
                        human[i].calories_taken = human[i].calories_taken+result;
                        String yaz =(human[i].id +"\t"+ "has taken"+"\t"+result+"kcal"+"\t"+"from"+"\t"+porsion[y].name+"\n");
                        food_writer.writer_food(yaz);
                        break;
                    }
                }
            break;
            }
        }
    }
}
