import java.io.IOException;
import java.util.List;

public abstract class sort {


    public int get_Max(int array[],int length){
        int max=array[0];
        for(int i=1;i<length;i++){
            if(array[i]>max)
                max=array[i];
        }
        return max;
    }

    public void file_writer(List<Double> average_case, List<Double> best_case, List<Double> worst_case) throws IOException{}
}
