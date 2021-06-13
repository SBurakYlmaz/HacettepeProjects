import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;


public class Merge extends  sort{


    public void Merge_sort(int array[],int low,int high){
        if(low>=high) return;
        int middle = low+(high-low)/2;
        Merge_sort(array,low,middle);
        Merge_sort(array,middle+1,high);
        merge_operation(array,low,middle,high);
    }

    public void merge_operation(int array[],int left_start,int right_start,int end){
        int auxiliary_array[]=new int[array.length];
        int left_array_start=left_start;
        int middle=right_start+1;

        for (int k=left_start;k<=end;k++){
            auxiliary_array[k]=array[k];
        }
        for(int k=left_array_start;k<=end;k++){
            if(left_array_start>right_start){
                array[k]=auxiliary_array[middle];
                middle++;
            }
            else if(middle > end){
                array[k]=auxiliary_array[left_array_start];
                left_array_start++;
            }
            else if(auxiliary_array[left_array_start]<auxiliary_array[middle]){
                array[k]=auxiliary_array[left_array_start];
                left_array_start++;
            }
            else {
                array[k]=auxiliary_array[middle];
                middle++;
            }
        }
    }


    public void MergeAnalysis()throws IOException{
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
                Merge_sort(array,0,array.length-1);
                double time = stopwatch.elapsedTime();
                average+=time;
                flag++;
            }
            average_case_time.add(average/(double)200);
            average=0;
            flag=0;


            while(flag<200){
                Stopwatch stopwatch = new Stopwatch();
                Merge_sort(array,0,array.length-1);
                double time = stopwatch.elapsedTime();
                average+=time;
                flag++;
            }
            best_case_time.add(average/(double)200);
            flag=0;
            average=0;
            int auxiliary[]=new int[i];
            int end=auxiliary.length-1;


            while(flag<200){
                for (int anArray : array) {
                    auxiliary[end] = anArray;
                    end--;
                }
                Stopwatch stopwatch = new Stopwatch();
                Merge_sort(auxiliary,0,array.length-1);
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
        FileWriter fw = new FileWriter("output.txt",true);

        Double [] sorting_average =  new Double[average_case.size()];
        sorting_average = average_case.toArray(sorting_average);


        Double [] sorting_best =  new Double[best_case.size()];
        sorting_best = best_case.toArray(sorting_best);

        Double [] sorting_worst =  new Double[worst_case.size()];
        sorting_worst = worst_case.toArray(sorting_worst);


        fw.write("begin worst merge\n");
        for(int i=0;i<worst_case.size();i++)
            fw.write(String.format("%d,%.4f\n",i+1,sorting_worst[i]));

        fw.write("end\n\n");

        fw.write("begin average merge\n");
        for(int i=0;i<average_case.size();i++)
            fw.write(String.format("%d,%.4f\n",i+1,sorting_average[i]));

        fw.write("end\n\n");

        fw.write("begin best merge\n");
        for(int i=0;i<best_case.size();i++)
            fw.write(String.format("%d,%.4f\n",i+1,sorting_best[i]));

        fw.write("end\n\n");

        fw.close();
    }
}
