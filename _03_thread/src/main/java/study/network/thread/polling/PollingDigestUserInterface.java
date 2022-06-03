package study.network.thread.polling;

import study.network.thread.DigestThread;
import study.network.thread.ReturnDigest;

import javax.xml.bind.DatatypeConverter;
import java.net.URL;
import java.util.Objects;

public class PollingDigestUserInterface {
  
  public static void main(String[] args) {
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
		
	  for(int i = 0 ; i < args.length ; i++) {
			while(true) {
				byte[] digest = dr[i].getDigest();
				if(digest != null) {
					StringBuilder result = new StringBuilder(args[i]);
					result.append(": ");
					result.append(DatatypeConverter.printHexBinary(digest));
					System.out.println(result);
					break;
				}
			}
    }
  }
}