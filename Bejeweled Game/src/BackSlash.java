import java.util.List;
import java.util.Map;
public class BackSlash extends MathSymbols {
    @Override
    public String toString() {
        return "\\";
    }

    @Override
    //Looking the right direction and find the correct jewel or math symbol
    public List<Map> BejeweledDetector(List<Map> jeweled_list, int row, int column, Integer x_axis, Integer y_axis,List<BeJeweled> scorer,BeJeweled target) {
        if(x_axis-2 >= 0 && y_axis-2 >= 0){
            BeJeweled target1 = (BeJeweled) jeweled_list.get(x_axis-1).get(y_axis-1);
            BeJeweled target2 = (BeJeweled) jeweled_list.get(x_axis-2).get(y_axis-2);
            if(target1 instanceof BackSlash && target2 instanceof MathSymbols || target1 instanceof Wildcard){
                scorer.add(target1);scorer.add(target2);scorer.add(target);

                jeweled_list.get(x_axis-1).remove(y_axis-1);
                jeweled_list.get(x_axis-2).remove(y_axis-2);
                jeweled_list.get(x_axis).remove(y_axis);
                Score(scorer);
                return jeweled_list;
            }
        }
        if(row > x_axis+2 && column > y_axis+2){
            BeJeweled target1 = (BeJeweled) jeweled_list.get(x_axis+1).get(y_axis+1);
            BeJeweled target2 = (BeJeweled) jeweled_list.get(x_axis+2).get(y_axis+2);
            if(target1 instanceof BackSlash && target2 instanceof MathSymbols || target1 instanceof Wildcard){
                scorer.add(target1);scorer.add(target2);scorer.add(target);

                jeweled_list.get(x_axis+1).remove(y_axis+1);
                jeweled_list.get(x_axis+2).remove(y_axis+2);
                jeweled_list.get(x_axis).remove(y_axis);
                Score(scorer);
                return jeweled_list;
            }
        }
        return jeweled_list;
    }

}
