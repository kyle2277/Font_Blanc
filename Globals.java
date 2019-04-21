import java.util.*;
import java.io.*;
import java.lang.*;

public class Globals {
   
   // encryption object
   public EncoderDecoder_FB e;
   // user input path
   public String file_input;
   //name of file
   public String file;
   // path to file
   public String file_path;
   // declares whether to encrypt or decrypt file
   public String EorD;
   // apended to beginning of every encrypted file
   public static final String encrypt_tag = "encrypted_";
   // deprecated
   public static final String decrypt_tag = "decrypted_";
   // file type of encrypted file
   public static final String encrypted_ext = ".txt";
   // path to log file
   public static final String log_path = "/home/kylej/Documents/Kyle/Dev/ProjectFB/Font_Blanc/log.txt";
   
   public Globals(String file_input, String encodeKey, String EorD) {
      this.e = new EncoderDecoder_FB(encodeKey);
      this.file_input = file_input;
      this.file = file;
      this.file_path = file_path;
      this.EorD = EorD;
      split_file(file_input);
   }
   
   /*
   Splits the file path into two components: path to the file and the file name
   */
   public void split_file(String file_input) {
      String[] split = file_input.split("/", 0);
      if(split.length > 1) {
         file_path = "";
         //set global file path var
         for(int i = 1; i < split.length-1; i++) {
            file_path += "/" + split[i];
         }
         file_path += "/";
      } else {
         file_path = "./";
      }
      //File name var
      file = split[split.length-1];
   }

}