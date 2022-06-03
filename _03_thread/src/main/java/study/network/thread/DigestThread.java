package study.network.thread;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Thread의 서브 클래스
 */
public class DigestThread extends Thread {
	
	private final String filename;
	
	public DigestThread(String filename) {
		this.filename = filename;
	}
	
	/**
	 * 지정된 파일의 256비트 SHA-2 메시지 다이제스트를 계산
	 */
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
		} catch (IOException | NoSuchAlgorithmException ex) {
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
			Thread t = new DigestThread(filename);
			t.start();
		}
	}
}