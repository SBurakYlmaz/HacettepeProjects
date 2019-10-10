import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Radix extends sort{


    public void Radix_Sort(int array[]){
       int max=get_Max(array,array.length);
        int final_array[]=new int[array.length];
        int exponential=1;

        while (max / exponential > 0)
        {
            int[] result = new int[array.length];

            for (int anArray : array) result[(anArray / exponential) % 10]++;

            for (int j = 1; j < 10; j++)
                result[j] += result[j-1];

            for (int k =array.length - 1; k >= 0; k--)
                final_array[--result[(array[k] / exponential) % 10]] = array[k];

            System.arraycopy(final_array, 0, array, 0, array.length);
            exponential *= 10;
        }
    }

    public void RadixAnalysis()throws IOException {
        Random random = new Random();
        int top_boundary=50000;
        int increase_gap=5000;

        List<Double> average_case_time = new ArrayList<>();
        List<Double> best_case_time = new ArrayList<>();
        List<Double> worst_case_time = new ArrayList<>();

        for(int i=increase_gap;i<=top_boundary;i+=increase_gap){
            int array[]=new int[i];

            int flag=0;
            double average=0;


            while(flag<200){
                for(int j=0;j<array.length-1;j++)
                    array[j]=random.nextInt(400000);
                Stopwatch stopwatch = new Stopwatch();
                Radix_Sort(array);
                double time = stopwatch.elapsedTime();
                average+=time;
                flag++;
            }
            average_case_time.add(average/(double)200);
            average=0;
            flag=0;


            while(flag<200){
                Stopwatch stopwatch = new Stopwatch();
                Radix_Sort(array);
                double time = stopwatch.elapsedTime();
                average+=time;
                flag++;
            }
            best_case_time.add(average/(double)200);
            average=0;
            flag=0;

            int auxiliary[]=new int[i];
            int end=auxiliary.length-1;


            while(flag<200){
                for (int anArray : array) {
                    auxiliary[end] = anArray;
                    end--;
                }
                Stopwatch stopwatch = new Stopwatch();
                Radix_Sort(auxiliary);
                double time = stopwatch.elapsedTime();
                average+=time;
                flag++;
                end=auxiliary.length-1;
            }
            worst_case_time.add(average/(double)200);
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


        fw.write("begin worst radix\n");
        for(i=0;i<worst_case.size();i++)
            fw.write(String.format("%d,%.4f\n",i+1,sorting_worst[i]));

        fw.write("end\n\n");

        fw.write("begin average radix\n");

        for(i=0;i<average_case.size();i++)
            fw.write(String.format("%d,%.4f\n",i+1,sorting_average[i]));

        fw.write("end\n\n");

        fw.write("begin best radix\n");
        for(i=0; i<best_case.size(); i++)
            fw.write(String.format("%d,%.4f\n",i+1,sorting_best[i]));

        fw.write("end\n\n");

        fw.close();
    }
}
