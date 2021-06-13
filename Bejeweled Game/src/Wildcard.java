
import java.util.List;
import java.util.Map;

public class Wildcard extends Jewels {
    private final Integer point = 10;

    public Integer getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return "W";
    }

    //Looking the right direction and find the correct jewel or math symbol

    @Override
    public List<Map> BejeweledDetector(List<Map> jeweled_list, int row, int column, Integer x_axis, Integer y_axis,List<BeJeweled> scorer,BeJeweled target) {
        if (x_axis - 2 >= 0) {
            BeJeweled target1 = (BeJeweled) jeweled_list.get(x_axis - 1).get(y_axis);
            BeJeweled target2 = (BeJeweled) jeweled_list.get(x_axis - 2).get(y_axis);
            if (target1 instanceof Triangle && target2 instanceof Triangle || target1 instanceof Wildcard
                    || target1 instanceof Plus && target2 instanceof MathSymbols || target1 instanceof Pipe && target2 instanceof MathSymbols) {
                scorer.add(target1);scorer.add(target2);scorer.add(target);

                jeweled_list.get(x_axis - 1).remove(y_axis);
                jeweled_list.get(x_axis - 2).remove(y_axis);
                jeweled_list.get(x_axis).remove(y_axis);
                Score(scorer);
                return jeweled_list;
            }
        }
        if (row > x_axis + 2) {
            BeJeweled target1 = (BeJeweled) jeweled_list.get(x_axis + 1).get(y_axis);
            BeJeweled target2 = (BeJeweled) jeweled_list.get(x_axis + 2).get(y_axis);
            if (target1 instanceof Triangle && target2 instanceof Triangle || target1 instanceof Wildcard
                    || target1 instanceof Plus && target2 instanceof MathSymbols || target1 instanceof Pipe && target2 instanceof MathSymbols) {
                scorer.add(target1);scorer.add(target2);scorer.add(target);

                jeweled_list.get(x_axis + 1).remove(y_axis);
                jeweled_list.get(x_axis + 2).remove(y_axis);
                jeweled_list.get(x_axis).remove(y_axis);
                Score(scorer);
                return jeweled_list;
            }
        }
        if ( y_axis - 2 >= 0) {
            BeJeweled target1 = (BeJeweled) jeweled_list.get(x_axis).get(y_axis - 1);
            BeJeweled target2 = (BeJeweled) jeweled_list.get(x_axis).get(y_axis - 2);
            if (target1 instanceof Square && target2 instanceof Square || target1 instanceof Wildcard
                    || target1 instanceof Minus && target2 instanceof MathSymbols || target1 instanceof Plus && target2 instanceof MathSymbols) {
                scorer.add(target1);scorer.add(target2);scorer.add(target);

                jeweled_list.get(x_axis).remove(y_axis - 1);
                jeweled_list.get(x_axis).remove(y_axis - 2);
                jeweled_list.get(x_axis).remove(y_axis);
                Score(scorer);
                return jeweled_list;
            }

        }
        if (column > y_axis + 2) {
            BeJeweled target1 = (BeJeweled) jeweled_list.get(x_axis).get(y_axis + 1);
            BeJeweled target2 = (BeJeweled) jeweled_list.get(x_axis).get(y_axis + 2);
            if (target1 instanceof Square && target2 instanceof Square || target1 instanceof Wildcard
                    || target1 instanceof Minus && target2 instanceof MathSymbols || target1 instanceof Plus && target2 instanceof MathSymbols) {
                scorer.add(target1);scorer.add(target2);scorer.add(target);

                jeweled_list.get(x_axis).remove(y_axis + 1);
                jeweled_list.get(x_axis).remove(y_axis + 2);
                jeweled_list.get(x_axis).remove(y_axis);
                Score(scorer);
                return jeweled_list;
            }

        }
        if(x_axis-2 >= 0 && y_axis-2 >= 0){
            BeJeweled target1 = (BeJeweled) jeweled_list.get(x_axis-1).get(y_axis -1);
            BeJeweled target2 = (BeJeweled) jeweled_list.get(x_axis-2).get(y_axis - 2);
            if (target1 instanceof Square && target2 instanceof Square || target1 instanceof Wildcard
                    || target1 instanceof Slash && target2 instanceof MathSymbols) {
                scorer.add(target1);scorer.add(target2);scorer.add(target);

                jeweled_list.get(x_axis-1).remove(y_axis - 1);
                jeweled_list.get(x_axis-2).remove(y_axis + 2);
                jeweled_list.get(x_axis).remove(y_axis);
                Score(scorer);
                return jeweled_list;
            }
        }
        if(row > x_axis+2 && column > y_axis+2){
            BeJeweled target1 = (BeJeweled) jeweled_list.get(x_axis+1).get(y_axis + 1);
            BeJeweled target2 = (BeJeweled) jeweled_list.get(x_axis+2).get(y_axis + 2);
            if (target1 instanceof Square && target2 instanceof Square || target1 instanceof Wildcard
                    || target1 instanceof Slash && target2 instanceof MathSymbols) {
                scorer.add(target1);scorer.add(target2);scorer.add(target);

                jeweled_list.get(x_axis).remove(y_axis);
                jeweled_list.get(x_axis+1).remove(y_axis + 1);
                jeweled_list.get(x_axis+2).remove(y_axis + 2);
                Score(scorer);
                return jeweled_list;
            }
        }
        if(x_axis-2 >= 0 && column > y_axis+2){
            BeJeweled target1 = (BeJeweled) jeweled_list.get(x_axis-1).get(y_axis+1);
            BeJeweled target2 = (BeJeweled) jeweled_list.get(x_axis-2).get(y_axis+2);
            if(target1 instanceof Diamond && target2 instanceof Diamond|| target1 instanceof Wildcard
                    || target1 instanceof BackSlash && target2 instanceof MathSymbols){
                scorer.add(target1);scorer.add(target2);scorer.add(target);

                jeweled_list.get(x_axis-1).remove(y_axis+1);
                jeweled_list.get(x_axis-2).remove(y_axis+2);
                jeweled_list.get(x_axis).remove(y_axis);
                Score(scorer);
                return jeweled_list;
            }
        }
        if(row > x_axis+2 && y_axis-2 >= 0) {
            BeJeweled target1 = (BeJeweled) jeweled_list.get(x_axis + 1).get(y_axis - 1);
            BeJeweled target2 = (BeJeweled) jeweled_list.get(x_axis + 2).get(y_axis - 2);
            if (target1 instanceof Diamond && target2 instanceof Diamond || target1 instanceof Wildcard
                    || target1 instanceof BackSlash && target2 instanceof MathSymbols) {
                scorer.add(target1);scorer.add(target2);scorer.add(target);

                jeweled_list.get(x_axis + 1).remove(y_axis - 1);
                jeweled_list.get(x_axis + 2).remove(y_axis - 2);
                jeweled_list.get(x_axis).remove(y_axis);
                Score(scorer);
                return jeweled_list;
            }
        }
        return jeweled_list;
    }

}