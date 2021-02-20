import java.io.FileWriter;
import java.io.IOException;

public class CryptoLog
{
    private static CryptoLog sInstance;
    private static FileWriter sFileWriter;
    
    static {
        try {
            sInstance = new CryptoLog();
            sFileWriter = new FileWriter("log.txt");
        } catch (IOException e) {
            System.err.println(e.toString());
        }  
    }
    
    private CryptoLog()
    {
    }
    
    public static CryptoLog getInstance()
    {
        return sInstance;
    }
    
    public synchronized void write(String str)
    {
        try {
            sFileWriter.write(str);
            sFileWriter.flush();
            
            System.out.print(str);
            System.out.flush();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
    
    public synchronized void writeLine(String str)
    {
        try {
            sFileWriter.write(str + "\n");
            sFileWriter.flush();
            
            System.out.println(str);
            System.out.flush();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}
