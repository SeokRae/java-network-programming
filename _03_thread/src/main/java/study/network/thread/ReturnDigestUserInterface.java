package study.network.thread;

import javax.xml.bind.*; // for DatatypeConverter
import java.net.URL;
import java.util.Objects;

/**
 * 코드 구현 순서와는 다르게 동작하는 예시
 */
public class ReturnDigestUserInterface {
  
  public static void main(String[] args) {
	  URL resource = Objects.requireNonNull(
			  DigestThread.class
					  .getClassLoader()
					  .getResource("digest.txt"));
	  args = new String[]{resource.getPath()};
    for (String filename : args) {
      // Calculate the digest
      ReturnDigest dr = new ReturnDigest(filename);
      dr.start();
      
      // Now print the result
      StringBuilder result = new StringBuilder(filename);
      result.append(": ");
      byte[] digest = dr.getDigest();
      result.append(DatatypeConverter.printHexBinary(digest));
      System.out.println(result); 
    }
  }
}