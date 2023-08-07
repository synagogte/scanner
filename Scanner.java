package scanner;
import java.io.*;

/**
 * Scanner is a simple scanner for Compilers and Interpreters (2014-2015) lab exercise 1
 * @author Syna Gogte
 * @version January 28, 2022
 *  
 * Usage:
 * Scans an input and returns tokens for each of the different lexemes
 *
 */
public class Scanner
{
    private Reader in;
    private char currentChar;
    private boolean endOfFile;
    // define symbolic constants for each type of token
    //public static enum TOKEN_TYPE 
    //{WORD, END_OF_SENTENCE, END_OF_FILE, END_OF_PHRASE, DIGIT, UNKNOWN};
    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        endOfFile = false;
        getNextChar();
    }
    /**
     * Scanner constructor for constructing a scanner that 
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        endOfFile = false;
        getNextChar();
    }
    /**
     * sets the current character to the value read by the input stream 
     * changes the endOfFile instance field to true if the value is -1
     */
    private void getNextChar()
    { 
        try
        {
            int inp = in.read();
            if(inp == -1) 
                endOfFile = true;
            if((char)inp == '.')
                endOfFile = true;
            else 
                currentChar = (char) inp;
        }
        catch (IOException e)
        { 
            e.printStackTrace();
            System.exit(-1);
        }
    }
    /**
     * compares the expected value to the current value and moves on if the 
     * are the same. If they are not the same, it throws a ScanErrorException
     * @throws ScanErrorException if not the expected character
     */
    
    private void eat(char expected) throws ScanErrorException
    {
        if(expected == currentChar)
            getNextChar(); 
        else 
            throw new ScanErrorException("Illegal character, expected " + 
            expected + " and found " + currentChar); 
    }
    /**
     * tests whether there is another character after the current character
     * @return true if there is another character or false if the stream is 
     * at the end of the file
     */
    public boolean hasNext()
    {
        return !(endOfFile); 
    }
    
    /**
     * tests whether the char is a digit
     * @param c  the character being tested 
     * @return true if it is a digit or false otherwise
     */
    public static boolean isDigit(char c)
    {
        if(c >= '0' && c <= '9')
        {
            return true;
        }
        return false; 
    }
    /**
     * tests whether the char is a letter
     * @param c  the character being tested 
     * @return true if it is a letter or false otherwise
     */
    public static boolean isLetter(char c)
    {
        if((c >= 'A' && c<= 'Z')|| (c >= 'a' && c<= 'z'))
        {
            return true;
        }
        return false;  
    }
    /**
     * tests whether the char is white space
     * @param c  the character being tested 
     * @return true if it is white space or false otherwise
     */
    public static boolean isWhiteSpace(char c)
    {
        if(c == '\n' ||c == ' ' || c == '\t' || c == '\r' || c == ';') 
        {
            return true; 
        }
        return false; 
    }
    /**
     * tests whether the char is white space
     * @param c  the character being tested 
     * @return true if it is white space or false otherwise
     */
    public static boolean isOperand(char c)
    {
        
        if (c == '=' || c == '+' ||
                c == '-' || c == '*' ||
                c == '/' || c == '%' ||
                c == '(' || c == ')' || c == '<' || c  == '>' || c==':')
        {
            return true; 
        }
        return false; 
    }

    /**
     * scans a number. called if currentChar is an digit
     * @throws ScanErrorException if illegal character 
     */
    private String scanNumber() throws ScanErrorException 
    {
        String lexeme = ""; 
        if((isDigit(currentChar)))
        {
            lexeme+=currentChar; 
            eat(currentChar); 
            while(hasNext() && !(isWhiteSpace(currentChar)))
            {
                if((isDigit(currentChar)))
                {
                    lexeme+=currentChar; 
                    eat(currentChar); 
                }
            }
        }
        return lexeme; 
    }
    /**
     * scans an identifier. called if currentChar is an letter
     * @throws ScanErrorException if illegal character 
     */
    private String scanIdentifier() throws ScanErrorException 
    {
        String lexeme = ""; 
        if((isLetter(currentChar)))
        {
            lexeme += currentChar; 
            eat(currentChar); 
            while((hasNext() && (isLetter(currentChar)) || (isDigit(currentChar)))) 
            {
                lexeme += currentChar;
                eat(currentChar); 
            } 
        }
        return lexeme; 
        
    }
    /**
     * scans an operand. called if currentChar is an operand
     * @throws ScanErrorException if illegal character 
     */
    private String scanOperand() throws ScanErrorException
    {
        char val = currentChar; 
        
        if(val == '<')
        {
            eat(currentChar); 
            if(currentChar == '=' || currentChar == '>')
                return "" + val+currentChar; 
            else 
                throw new ScanErrorException("illegal character. unrecognizable token "+ val); 
            
        }
        else if(val == '>')
        {
            eat(currentChar); 
            if(currentChar == '=')
                return "" + val+currentChar;
            else
                throw new ScanErrorException("illegal character. unrecognizable token " + val); 
        }
        else if(val == ':')
        {
            eat(currentChar); 
            if(currentChar == '=')
                return "" + val+currentChar; 
            else
                throw new ScanErrorException("illegal character. unrecognizable token " + val); 
        }
        else if(val == '/')
        {
            eat(currentChar); 
            if(currentChar == '/')
            {
                while(currentChar != '\n')
                {
                    eat(currentChar); 
                }
                return ""; 
            }
            else
                return "" +val; 
        }
        else if (isOperand(currentChar))
        {
            eat(currentChar); 
            return "" + val; 
        }
        eat(currentChar); 
        return "";
    }

    /**
     * creates the tokens for the input
     * @return a string representing the lexeme that is found
     * @throws ScanErrorException if unrecognizable tokem 
     */
    public String nextToken() throws ScanErrorException
    {
        if(endOfFile)
        {
            return "END"; 
        }
        while(hasNext() && isWhiteSpace(currentChar))
        {
            eat(currentChar);
        }
        try
        {
            if(endOfFile)
            {
                return "END"; 
            }
            if(isDigit(currentChar))
                return scanNumber();
            if(isLetter(currentChar))
                return scanIdentifier();
            if(isOperand(currentChar))
            {
                String result = scanOperand();
                if(result.length() == 0)
                {
                    return nextToken();
                }
                return result;
            }  
            else 
                throw new ScanErrorException("unrecognized token " + currentChar);   
        }
        catch(ScanErrorException e)
        {
            e.printStackTrace();
            System.exit(-1); 
            return "error"; 
        }
        
    }    
}
