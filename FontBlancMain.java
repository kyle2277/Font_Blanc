import java.util.*;
import java.io.*;
import java.lang.*;
import org.ejml.simple.*;

public class FontBlancMain {
   
   public static EncoderDecoder_FB e;
   // path to file
   public static String file;
   // declares whether to encrypt or decrypt file
   public static String EorD;
   public static final String encrypt_tag = "encrypted_";
   public static final String decrypt_tag = "decrypted_";
   
   // TODO variable encrypted vector size
   
	public static void main(String[] args) throws IOException {
      System.out.println("Font Blanc");
      if(args.length == 3) {
         // run program
         file = args[0];
         String encodeKey = args[1];
         EorD = args[2];
         e = new EncoderDecoder_FB(encodeKey);
         if(e.fatal) {
            fatal("Inviable encryption key");
         }
         if(EorD.equalsIgnoreCase("encrypt")) {
            System.out.println("Encrypt");
            encrypt();   
         } else if(EorD.equalsIgnoreCase("decrypt")) {
            System.out.println("Decrypt");
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
      FileWriter out = null;
      try {
         File output = new File(encrypt_tag + file);
         output.delete();
         in = new FileInputStream(file);
         out = new FileWriter(output, true);
         boolean run = true;
         while(run) {
            byte[] unencrypted_vec = new byte[4];
            int off = 0;
            int len = 4;
            if (in.read(unencrypted_vec, off, len) != -1) {
               SimpleMatrix encrypted_vec = e.gen_safe_vec(unencrypted_vec);
               for(int i = 0; i < 4; i++) {
                  System.out.print((byte)unencrypted_vec[i] + " ");
                  out.write(Math.round(encrypted_vec.get(i,0)) + "\n");
               }
               System.out.println();
            } else {
               run = false;
            }
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
   
   public static void decrypt() throws IOException {
      Scanner in = null;
      FileOutputStream out = null;
      try {
         File input = new File(encrypt_tag + file);
         in = new Scanner(input);
         File output = new File(decrypt_tag + file);
         output.delete();
         out = new FileOutputStream(decrypt_tag + file);
         while(in.hasNextLine()) {
            double[][] safe_vec = new double[4][1];
            for(int i = 0; i < 4; i++) {
               if(in.hasNextLine()) {
                  String line = in.nextLine();
                  line = line.replace("\n", "");
                  safe_vec[i][0] = Double.valueOf(line);
               } else {
                  safe_vec[i][0] = 0.0;
               }
            }
            SimpleMatrix decrypted = e.gen_real_mat(safe_vec);
            for(int i = 0; i < 4; i++) {
               System.out.print((byte) Math.round(decrypted.get(i,0)) + " ");
               out.write((byte) Math.round(decrypted.get(i,0)));
            }
            System.out.println();
         }
      } catch(FileNotFoundException e) {
         fatal("Input file\"" + file + "\" not found");
      } finally {
         if(in != null) {
            in.close();
         }
         if(out != null) {
            out.close();
         }
      }
   }
   
}