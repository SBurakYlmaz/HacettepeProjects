import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Insertion extends sort{

    public void Insertion_Sort(int array[]){
        for(int i=1;i<array.length;i++){
            for(int j=i;j>0;j--){
                if(array[j]<array[j-1]){
                    int temp= array[j];
                    array[j]=array[j-1];
                    array[j-1]=temp;
                }
                else break;
            }
        }
    }

    public void InsertionAnalysis()throws IOException {
        Random random = new Random();
        int top_boundary=10000;
        int increase_gap=1000;

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
                Insertion_Sort(array);
                double time = stopwatch.elapsedTime();
                average+=time;
                flag++;
            }
            average_case_time.add(average/(double)200);
            average=0;
            flag=0;


            while(flag<200){
                Stopwatch stopwatch = new Stopwatch();
                Insertion_Sort(array);
                double time = stopwatch.elapsed_Time_nano();
                average+=time;
                flag++;
            }
            best_case_time.add(average/(double)200);
            average=0;
            flag=0;

            int auxiliary[]=new int[i];
            int end=auxiliary.length-1;


            while(flag<200){
                for(int j=0;j<array.length;j++) {
                    auxiliary[end]=  array[j] ;
                    end--;
                }
                Stopwatch stopwatch = new Stopwatch();
                Insertion_Sort(auxiliary);
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

        Double [] sorting_best =  new Double[average_case.size()];
        sorting_best = best_case.toArray(sorting_best);

        Double [] sorting_worst =  new Double[average_case.size()];
        sorting_worst = worst_case.toArray(sorting_worst);


        fw.write("begin worst insertion\n");
        for(i=0;i<worst_case.size();i++)
            fw.write(String.format("%d,%.4f\n",i+1,sorting_worst[i]));

        fw.write("end\n\n");

        fw.write("begin avr insertion\n");
        for(i=0;i<average_case.size();i++)
            fw.write(String.format("%d,%.4f\n",i+1,sorting_average[i]));


        fw.write("end\n\n");

        fw.write("begin best insertion\n");
        for(i=0;i<best_case.size();i++)
            fw.write(String.format("%d,%.2f\n",i+1,sorting_best[i]));


        fw.write("end\n\n");

        fw.close();
    }
}
