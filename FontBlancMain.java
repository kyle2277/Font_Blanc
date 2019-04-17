import java.util.*;
import java.io.*;
import java.lang.*;
import java.sql.Timestamp;
import org.ejml.simple.*;

public class FontBlancMain {
   
   // TODO variable encrypted vector size
   
	public static int main(String file_input, String encodeKey, String EorD) throws IOException {
      System.out.println("Font Blanc");
      Globals globals = new Globals(file_input, encodeKey, EorD);
      if(globals.e.fatal) {
         return fatal(globals, "Inviable encryption key");
      }
      boolean encrypt;
      if(encrypt = globals.EorD.equalsIgnoreCase("encrypt")) {
         System.out.println("Encrypt");
         FileInputStream in = null;
         FileWriter out = null;
         return distributor(globals, encrypt, in, out, null, null);
         //encrypt();   
      } else if(globals.EorD.equalsIgnoreCase("decrypt")) {
         System.out.println("Decrypt");
         Scanner in = null;
         FileOutputStream out = null;
         return distributor(globals, encrypt, null, null, in, out);
         //decrypt();
      } else {
         return fatal(globals, "Invalid arguments");
      }
	}
   
   public static int num(String one) {
      File f = new File(one);
      if(f.exists()) {
         return 1;
      } else {
         return 0;
      }
   }   
   
   /*
   Triggered if a fatal error occurs. Writes the error to the console and log file 
   before program termination
   */
   public static int fatal(Globals globals, String message) throws IOException {
      File fatal = new File(globals.log_path);
      FileWriter out = new FileWriter(fatal, true);
      Timestamp time = new Timestamp(System.currentTimeMillis());
      out.write(time + "\n");
      out.write("Fatal error:\n");
      out.write(message + "\n\n");
      System.out.println("Fatal error:");
      System.out.println(message);
      out.close();
      return 0;
   }
   
   /*
   Sends files to encryption or decryption
   */
   public static int distributor(Globals globals, boolean encrypt, FileInputStream en_in, FileWriter en_out, 
                                 Scanner de_in, FileOutputStream de_out) throws IOException {
      try {
         if(encrypt) {
            File output = new File(globals.file_path + globals.encrypt_tag + globals.file + globals.encrypted_ext);
            if(output.exists()) {
               output.delete();
            }
            en_in = new FileInputStream(globals.file_path + globals.file);
            en_out = new FileWriter(output);
            encrypt(globals, en_in, en_out);
         } else { //decrypt
            String file_out = globals.file_path + globals.file;
            File input = new File(globals.file_path + globals.encrypt_tag + globals.file + globals.encrypted_ext);
            de_in = new Scanner(input);
            File output = new File(file_out);
            if(output.exists()) {
               output.delete();
            }
            de_out = new FileOutputStream(file_out);
            decrypt(globals, de_in, de_out);
         }
      } catch(FileNotFoundException e) {
         return fatal(globals, "Input file \"" + globals.file_path + globals.file + "\" not found");
      } finally {
         if (en_in != null) {    en_in.close();    }
         if(en_out != null) {    en_out.close();   }
         if (de_in != null) {    de_in.close();    }
         if(de_out != null) {    de_out.close();   }
      }
      return 1;
   }
   
   /*
   Separates input bytes into vectors of length n, encrypts with change of basis operation
   Changes all 0 bytes at end of file to EOF character
   */
   public static void encrypt(Globals globals, FileInputStream in, FileWriter out) throws IOException {
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
            SimpleMatrix encrypted_vec = globals.e.gen_safe_vec(unencrypted_vec);
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
   }
   
   /*
   Reads encrypted bytes and puts them into vectors of length n. Performs reverse change of basis to decrypt
   Does not write bytes designated as EOF characters
   */
   public static void decrypt(Globals globals, Scanner in, FileOutputStream out) throws IOException {
      while(in.hasNextLine()) {
         double[][] safe_vec = new double[4][1];
         for(int i = 0; i < 4; i++) {
            if(in.hasNextLine()) {
               String line = in.nextLine();
               line = line.replace("\n", "");
               safe_vec[i][0] = Double.valueOf(line);
            }
         }
         SimpleMatrix decrypted = globals.e.gen_real_mat(safe_vec);
         for(int i = 0; i < 4; i++) {
				//prevents writing extra zero value bytes at end of file
				if((Math.round(decrypted.get(i,0)) != -1) || (in.hasNextLine())) {
					System.out.print((byte) Math.round(decrypted.get(i,0)) + " ");
               out.write((byte) Math.round(decrypted.get(i,0)));
				}
         }
         System.out.println();
      }   
   }
   
}