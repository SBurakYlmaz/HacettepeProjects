
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class BeJeweled {
    public abstract List<Map> BejeweledDetector(List<Map> maps,int row,int column,Integer x_axis,Integer y_axis,List<BeJeweled> scorer,BeJeweled target);

    public abstract List<BeJeweled> Score(List<BeJeweled> scorer);

    public abstract Integer getPoint();

    //Writing grid to the screen

    public void Grid_writer(List<Map> jeweledList,int column,int row){
        for (int i=0;jeweledList.size()>i;i++){
            for(int j =0;row>j;j++){
                if(jeweledList.get(i).get(j)== null)
                    System.out.print("   ");
                else {
                    System.out.print(jeweledList.get(i).get(j));
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    public void Grid_Shifter(List<Map> jeweledList,int row,int column){   //Shifting Grid Map
        BeJeweled shifter; BeJeweled temp;boolean control;int value,controller;
        for (int i=row-1;i>=0;i--){
            for(int j=column-1;j>=0;j--){
                value=i;
                controller=i;
                control = true;
                shifter = (BeJeweled) jeweledList.get(i).get(j);
                while (shifter==null && control) {
                    if (value - 1 >= 0) {
                        temp = (BeJeweled) jeweledList.get(value-1).get(j);
                        shifter = temp;
                        if(temp==null){
                            value--;
                            continue;
                        }
                        else
                            value--;
                        jeweledList.get(controller).put(j, shifter);
                        controller--;
                        jeweledList.get(value).remove(j);
                        shifter=(BeJeweled) jeweledList.get(value).get(j);
                    }
                    else
                        control = false;
                }
            }
        }
    }
    public int Score_Writer(List<BeJeweled> scorer){   //Writing the screen to score status for one round
        int score=0;
        for(BeJeweled beJeweled : scorer){
            score= score + beJeweled.getPoint();
        }
        if(score==0){
            System.out.println("Selected coordinate does not catch any triple match");
        }
        System.out.println(String.format("Score: %d points",score));
        return score;
    }
    public void leaderboard_writer(List<Players> players, File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);

        //If same player name it looks the point they have and remove the less point

        for (int i =0;i<players.size();i++){
            for(int j =1;j<players.size();j++){
                if(players.get(i).getName().equals(players.get(j).getName())){
                    if(players.get(i).getPoint().equals(players.get(j).getPoint()))
                        continue;
                    else
                        players.remove(j);
                }
            }
        }
        for(Players players1 : players)
            fileWriter.write(players1.toString());
        fileWriter.close();
    }
}
