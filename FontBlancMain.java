import java.util.*;

public class FontBlancMain {
   
   public static EncoderDecoder e;
   // path to file
   public static String file;
   // declares whether to encrypt or decrypt file
   public static String EorD;
   // passkey used for encoding and decoding
   public static String encodeKey;
   // output will be written to this file
   public File output;
   
	public static void main(String[] args) {
		System.out.println("Font Blanc");
      if(args.length() == 3) {
         // run program
         this.file = args[0];
         this.encodeKey = arsg[1];
         this.EorD = args[2];
         CharacterList list = new CharacterList("dictionary.txt");
         if(list.fatal) {
            fatal("no dictionary");
         }
         e = new EncoderDecoder(this.encoderKey, list);
      } else {
         fatal("Not enough arguments");
      }
	}
   
   public void fatal(String message) {
      System.out.println("Fatal error:");
      System.out.println(message);
      // write fatal message to output file
   }

}
