import java.util.*;
import java.io.*;
import org.ejml.simple.*;

/*
core encoder and decoder
requires and encoder key to be accessed
*/
public class EncoderDecoder_FB {
   
   //master password
   private String encodeKey;
   //change of basis matrix
   public SimpleMatrix encryptionMatrix;
   public boolean fatal = false;
   // end of file byte indicator
   public final int EOF = -1;
   
   
   //constructs encoder object, takes master password and dictionary   
   public EncoderDecoder_FB(String encodeKey) {
      this.encodeKey = encodeKey;
      generateMat(encodeKey);
   }
   
   /*
   Takes byte array and returns byte vector to be encrypted
   */
   public SimpleMatrix gen_safe_vec(byte[] plain_vec) {
      double[][] intermediate = new double[4][1];
      for(int i = 0; i < 4; i++) {
         double byte_val = (double) plain_vec[i];
         if(i >= plain_vec.length) {
            intermediate[i][0] = EOF;
         } else {
            intermediate[i][0] = byte_val;
         }
      } 
      SimpleMatrix unencrypted_vec = new SimpleMatrix(intermediate);
      return encryptVec(unencrypted_vec);
   }
   
   /*
	Encrypt using change of basis matrix
	takes vector of bytes to encode
   returns encrytped vector
   */
	public SimpleMatrix encryptVec(SimpleMatrix unencrypted_vec) {
      return encryptionMatrix.mult(unencrypted_vec);
   }
   
   /*
   Takes encoded byte array and returns unencrypted byte arrray
   */
   public SimpleMatrix gen_real_mat(double[][] safe_vec, SimpleMatrix decryptionMat) {
      SimpleMatrix encrypted_vec = new SimpleMatrix(safe_vec);
      return decryptVec(encrypted_vec, decryptionMat);
   }
   
   /*
	Takes encrypted vector and applies change of basis to un-encrypt
	returns unencrypted vector
	*/
   public SimpleMatrix decryptVec(SimpleMatrix encrypted_vec, SimpleMatrix decryptionMat) {
      //decryption matrix = inverse of encryption matrix
      SimpleMatrix decrypted_vec = decryptionMat.mult(encrypted_vec);
      return decrypted_vec;
   }
   
   //generates unique change of basis matrix from given master password
	//takes master password, sets encryption matrix with result 
   public void generateMat(String encodeKey) {
      double sum1 = 0;
      double sum2 = 0;
		//sums char values of master password in 2 ways
      for (int i = 0; i < encodeKey.length(); i++) {
			sum1 += encodeKey.charAt(i);
         sum2 += (encodeKey.charAt(i))*(Math.log(encodeKey.charAt(i)));
      }
		//take log of both sums to get unique, repeatable digits to use in change of basis matrix
      double nums = Math.log(sum1);
      double nums2 = Math.log(sum2);
      //System.out.println(nums + "\n" + nums2);
      String numStr = nums+"";
      String numStr2 = nums2+"";
      numStr= numStr.replaceAll("[.]", "");
      numStr2 = numStr2.replaceAll("[.]", "");
      numStr = extend(numStr);
      numStr2 = extend(numStr2);
      ArrayList<Double> contain = new ArrayList<Double>();
      //get matrix values by pairing same index values of the two sums
		for (int j = 0; j < Math.min(numStr.length(),numStr2.length()); j++) {
         String str1 = numStr.substring(j,j+1);
         String str2 = numStr2.substring(j,j+1);
         String union = str1+str2;
         double union_double = (double) Integer.parseInt(union);
         contain.add(union_double);
      } 
      double[][] matArray = populateMat(contain);
      SimpleMatrix gen = new SimpleMatrix(matArray);
      if (gen.determinant() == 0) {
         System.out.println("Invalid password");
         fatal = true;
      } else {
         encryptionMatrix = gen;
         encryptionMatrix.print();
      }
   }
   
	/*
	extends numbers with zeros in case they are too short to pair all values evenly
	for creation of change of basis matrix
	*/
   public String extend(String numStr) {
      int length = numStr.length();
      if (length < 16) {
         StringBuilder str = new StringBuilder(numStr);
         for (int i = 0; i < (16 - length); i++) {
            str.append("0");
         }
         numStr = str.toString();
      }
      return numStr;
   }
   
   /*
	takes values from number representation of encryption code and puts into 
   the change of basis matrix
   */
	public double[][] populateMat(ArrayList<Double> contain) {
      double[][] matArray = new double[4][4];
      int count = 0;
      for (int k = 0; k < 4; k++) {
         for (int w = 0; w < 4; w++) {
            //System.out.print(contain.get(count) + " ");
            matArray[k][w] = contain.get(count);
            count++;
         }
        // System.out.println();
      }
      return matArray;
   }
   
}