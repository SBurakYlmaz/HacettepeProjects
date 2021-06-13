import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Binarysearch {

    int binary_search(int array[],int key){
        int left=0;
        int right=array.length-1;
        while (left<=right){
            int middle=(left+right)/2;
            if(array[middle]==key)
                return middle;
            else if (array[middle]>key)
                right=middle-1;
            else
                left=middle+1;
        }
        return -1;
    }
    public void BinarysearchAnalysis()throws IOException {
        Random random = new Random();
        int top_boundary=20000;
        int increase_gap=2000;
        Radix radix =new Radix();

        List<Double> average_case_time = new ArrayList<>();
        List<Double> best_case_time = new ArrayList<>();
        List<Double> worst_case_time = new ArrayList<>();
        for(int i=increase_gap;i<=top_boundary;i+=increase_gap){
            int array[]=new int[i];
            int flag=0;
            double average=0;
            for(int j=0;j<array.length-1;j++)
                array[j]=random.nextInt(400001);
            radix.Radix_Sort(array);

            average=0;

            while(flag<1000){
                Stopwatch stopwatch = new Stopwatch();
                binary_search(array,random.nextInt(400001));
                double time = stopwatch.elapsed_Time_nano();
                average+=time;
                flag++;
            }
            average_case_time.add(average/1000.0);
            average=0;
            flag=0;
            while(flag<1000){
                Stopwatch stopwatch = new Stopwatch();
                binary_search(array,array[(array.length-1)/2]);
                double time = stopwatch.elapsed_Time_nano();
                average+=time;
                flag++;
            }
            best_case_time.add(average/1000.0);
            average=0;
            flag=0;

            while(flag<1000){
                Stopwatch stopwatch = new Stopwatch();
                binary_search(array,6000000);
                double time = stopwatch.elapsed_Time_nano();
                average+=time;
                flag++;
            }
            worst_case_time.add(average/1000.0);
        }
        file_writer(average_case_time,best_case_time,worst_case_time);
    }

    public void file_writer(List<Double> average_case,List<Double> best_case,List<Double> worst_case) throws IOException {
        int i;
        FileWriter fw = new FileWriter("output.txt",true);

        Double [] sorting_average =  new Double[average_case.size()];
        sorting_average = average_case.toArray(sorting_average);


        Double [] sorting_best =  new Double[best_case.size()];
        sorting_best = best_case.toArray(sorting_best);

        Double [] sorting_worst =  new Double[worst_case.size()];
        sorting_worst = worst_case.toArray(sorting_worst);

        fw.write("begin worst binary\n");
        for(i=0;i<worst_case.size();i++)
            fw.write(String.format("%d,%.2f\n",i+1,sorting_worst[i]));

        fw.write("end\n\n");

        fw.write("begin avr binary\n");
        for(i=0;i<average_case.size();i++)
            fw.write(String.format("%d,%.2f\n",i+1,sorting_average[i]));

        fw.write("end\n\n");

        fw.write("begin best binary\n");
        for(i=0; i<best_case.size(); i++)
            fw.write(String.format("%d,%.2f\n",i+1,sorting_best[i]));

        fw.write("end\n\n");

        fw.close();
    }
}
