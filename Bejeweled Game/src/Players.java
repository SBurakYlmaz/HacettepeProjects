
import java.util.List;
import java.util.Objects;
public class Players implements Comparable<Players>{
    private String name;
    private Integer point;

    public Players(String name, Integer point) {
        this.name = name;
        this.point = point;
    }

    public String getName() {

        return name;
    }

    public Integer getPoint() {
        return point;
    }
    @Override
    public String toString() {
        return  name + " " +point+"\n" ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Players)) return false;
        Players players = (Players) o;
        return Objects.equals(name, players.name) &&
                Objects.equals(point, players.point);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, point);
    }

    public boolean equals_detector(List<Players> players, Players current_player){
        for(Players players1 : players){
            if(players1.equals(current_player))
                return false;
        }
        return true;
    }

    public void Rank_Writer(List<Players> players, int index){

        if (index!=0&&index!=players.size()-1){
            int higher = players.get(index).getPoint()-players.get(index+1).getPoint();
            int lower = players.get(index-1).getPoint()-players.get(index).getPoint();
            if(lower==0 && higher==0)
                System.out.println(String.format("Your rank %d/%d,your are same score with %s and %s\n"
                        ,index+1,players.size(),players.get(index+1).getName(),players.get(index-1).getName()));
            else if(higher==0)
                System.out.println(String.format("Your rank %d/%d,your score is  equals with %s and %d points lower than %s\n"
                        ,index+1,players.size(),players.get(index+1).getName(),lower,players.get(index-1).getName()));
            else if(lower==0)
            System.out.println(String.format("Your rank %d/%d,your score is %d points higher than %s and  equals with %s\n"
                    ,index+1,players.size(),higher,players.get(index+1).getName(),players.get(index-1).getName()));

            else
                System.out.println(String.format("Your rank %d/%d,your score is %d points higher than %s and %d points lower than %s\n"
                ,index+1,players.size(),higher,players.get(index+1).getName(),lower,players.get(index-1).getName()));
        }
        else if(index==0){
            int higher = players.get(index).getPoint()-players.get(index+1).getPoint();
            if(players.size()==1)
                System.out.println("Your rank 1/1 and no one played this game before");
            else {
                if(higher==0)
                    System.out.println(String.format("Your rank %d/%d,your are same score with %s",1,players.size(),players.get(index+1).getName()));
                else
                System.out.println(String.format("Your rank %d/%d your score is %d points higher than %s and there is no one above you\n",
                    1,players.size(),higher,players.get(index+1).getName()));
            }
        }
        else if(index==players.size()-1){
            int lower = players.get(index-1).getPoint()-players.get(index).getPoint();
            System.out.println(String.format("Your rank %d/%d you are the last there is no one below you and your score is %d points lower than %s\n",
                    players.size(),players.size(),lower,players.get(index-1).getName()));
        }
        System.out.println("\nGood Bye!");
    }

    @Override
    public int compareTo(Players o) {
        return -point.compareTo(o.point);
    }
}
