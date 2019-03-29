import java.util.*;
import java.io.*;
import org.ejml.simple.*;

public class FontBlancMain {
   
   public static EncoderDecoder e;
   // path to file
   public static String file;
   // declares whether to encrypt or decrypt file
   public static String EorD;
   // output will be written to this file
   public static File output;
   public static File input;
   public static boolean end = false;
   // end of file byte indicator
   public static final int EOF = 999;
   
	public static void main(String[] args) throws IOException {
      System.out.println("Font Blanc");
      if(args.length == 3) {
         // run program
         file = args[0];
         String encodeKey = args[1];
         EorD = args[2];
         CharacterList list = new CharacterList("dictionary.txt");
         if(list.fatal) {
            fatal("no dictionary");
         }
         e = new EncoderDecoder(encodeKey, list);
         if(EorD.equalsIgnoreCase("encrypt")) {
            encrypt();   
         } else if(EorD.equalsIgnoreCase("decrypt")) {
            decrypt();
         } else {
            fatal("Invalid arguments");
         }
      } else {
         fatal("Not enough arguments");
      }
	}
   
   public static void fatal(String message) {
      System.out.println("Fatal error:");
      System.out.println(message);
      // write fatal message to output file before exit
      System.exit(0);
   }
   
   public static void encrypt() throws IOException {
      FileInputStream in = null;
      FileOutputStream out = null;
      try {
         in = new FileInputStream(file);
         out = new FileOutputStream("safe_" + file);
         while(!end) {
            double[][] plain_vec = fours(in);
            SimpleMatrix safe = e.gen_safe_mat(plain_vec);
            for(int i = 0; i < 4; i++) {
               System.out.print((int)safe.get(i,0) + " ");
               out.write((int)safe.get(i,0));
            }
            System.out.println();
         }
      } catch(FileNotFoundException e) {
         fatal("Input file \"" + file + "\" not found");
      } finally {
         if(in != null) {
            in.close();
         }
         if(out != null) {
            out.close();
         }
      }
   }
   
   public static double[][] fours(FileInputStream in) throws IOException {
      int c;
      double[][] plain_vec = new double[4][1];
      for(int i = 0; i < 4; i++) {
         if((c = in.read()) != -1) {
            System.out.print(c + " ");
            plain_vec[i][0] = c;
         } else {
            System.out.print(EOF + " ");
            plain_vec[i][0] = EOF;
            end = true;
         }
      }
      System.out.println();
      return plain_vec;
   }
   
   public static void decrypt() {
      
   }
}