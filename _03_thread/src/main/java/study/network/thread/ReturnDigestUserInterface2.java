package study.network.thread;

import javax.xml.bind.*; // for DatatypeConverter
import java.net.URL;
import java.util.Objects;

public class ReturnDigestUserInterface2 {
  
  public static void main(String[] args) throws InterruptedException {
	  URL resource = Objects.requireNonNull(
			  DigestThread.class
					  .getClassLoader()
					  .getResource("digest.txt"));
	  args = new String[]{resource.getPath()};
	  ReturnDigest[] dr = new ReturnDigest[args.length];
		for(int i = 0 ; i < args.length ; i++) {
			dr[i] = new ReturnDigest(args[i]);
			dr[i].start();
		}
		
	  // 위의 스레드가 모두 수행된 뒤에 아래 스레드가 실행될 수 있도록 sleep
		Thread.sleep(100);
		
	  for(int i = 0 ; i < args.length ; i++) {
      // Now print the result
      StringBuilder result = new StringBuilder(args[i]);
      result.append(": ");
      byte[] digest = dr[i].getDigest();
      result.append(DatatypeConverter.printHexBinary(digest));
      System.out.println(result); 
    }
  }
}