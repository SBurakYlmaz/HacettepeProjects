import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;


public class Assignment1 {

    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.US);
        FileWriter fw = new FileWriter("output.txt",false);
        fw.write("");

        Merge merge = new Merge();
        Insertion insertion = new Insertion();
        Radix radix = new Radix();
        Binarysearch binarysearch = new Binarysearch();
        Selection selection = new Selection();

        radix.RadixAnalysis();
        selection.SelectionAnalysis();
        insertion.InsertionAnalysis();
        merge.MergeAnalysis();
        binarysearch.BinarysearchAnalysis();
    }
}