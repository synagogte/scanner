package scanner;
import java.io.*; 

/**
 * Reads a file that contains code to test the scanner
 *
 * @author Syna Gogte
 * @version January 26, 2022
 */
public class ScannerTester
{
    // instance variables - replace the example below with your own


    /**
     * Constructor for objects of class ScannerTester
     * @param args 
     */
    public static void main(String[] args)
    {
        // initialise instance variables
        try
        {
            FileInputStream file;
            file = new FileInputStream
            ("/Users/synagogte/Desktop/compilers labs/ScannerTest.txt"); 
            Scanner scanner = new Scanner(file);
            int count = 1;
            while (scanner.hasNext())
            {
                
                System.out.println("Token " + count + ": " + scanner.nextToken()); 
                count++;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
