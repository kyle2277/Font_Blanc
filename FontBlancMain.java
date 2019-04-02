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
   // TODO encryption file must be txt
   
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
         File output = new File(encrypt_tag + file + ".txt");
         output.delete();
         in = new FileInputStream(file);
         out = new FileWriter(output, true);
         boolean last = false;
         byte[] unencrypted_vec = new byte[4];
         int off = 0;
         int len = 4;
         if(in.read(unencrypted_vec, off, len) != -1) {
            while(!last) {
               byte[] unencrypted_vec_nxt = new byte[4];
               if (in.read(unencrypted_vec_nxt, off, len) == -1) {
                  for(int i = 0; i < 4; i++) {
                     if(unencrypted_vec[i] == 0) {
                        unencrypted_vec[i] = -1;
                     }
                  }
                  last = true;
               }
               SimpleMatrix encrypted_vec = e.gen_safe_vec(unencrypted_vec);
               //System.out.println(Arrays.toString(unencrypted_vec));
               for(int i = 0; i < 4; i++) {
                  System.out.print((byte)unencrypted_vec[i] + " ");
                  //System.out.println(encrypted_vec.get(i,0) + " ");
                  out.write(Math.round(encrypted_vec.get(i,0)) + "\n");
               }
               System.out.println();
               unencrypted_vec = unencrypted_vec_nxt;
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
   
   public static byte[] check_last(byte[] unencrypted_vec) {
      if(Arrays.asList(unencrypted_vec).contains(-1)) {
         int i = Arrays.asList(unencrypted_vec).indexOf(-1);
         for (int j = i; j < 4; j++) {
            if(unencrypted_vec[i] == 0) {
               unencrypted_vec[i] = -1;
            }
         }
      }
      return unencrypted_vec;
   }
   
   public static void decrypt() throws IOException {
      Scanner in = null;
      FileOutputStream out = null;
      try {
         File input = new File(encrypt_tag + file + ".txt");
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
               }
            }
            SimpleMatrix decrypted = e.gen_real_mat(safe_vec);
            for(int i = 0; i < 4; i++) {
               System.out.print((byte) Math.round(decrypted.get(i,0)) + " ");
					//prevents writing extra zero value bytes at end of file
					if((Math.round(decrypted.get(i,0)) != -1) || (in.hasNextLine())) {
						out.write((byte) Math.round(decrypted.get(i,0)));
					}
               
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