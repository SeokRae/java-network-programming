package study.network.thread;

import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * 상속 대신 구현을 통한 스레드 생성
 */
public class DigestRunnable implements Runnable {
	
	private final String filename;
	
	public DigestRunnable(String filename) {
		this.filename = filename;
	}
	
	@Override
	public void run() {
		try {
			FileInputStream in = new FileInputStream(filename);
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			DigestInputStream din = new DigestInputStream(in, sha);
			while (din.read() != -1) ;
			din.close();
			byte[] digest = sha.digest();
			
			StringBuilder result = new StringBuilder(filename);
			result.append(": ");
			result.append(DatatypeConverter.printHexBinary(digest));
			System.out.println(result);
		} catch (IOException ex) {
			System.err.println(ex);
		} catch (NoSuchAlgorithmException ex) {
			System.err.println(ex);
		}
	}
	
	public static void main(String[] args) {
		URL resource = Objects.requireNonNull(
				DigestThread.class
						.getClassLoader()
						.getResource("digest.txt"));
		args = new String[]{resource.getPath()};
		for (String filename : args) {
			DigestRunnable dr = new DigestRunnable(filename);
			Thread t = new Thread(dr);
			t.start();
		}
	}
}