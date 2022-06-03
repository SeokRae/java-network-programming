package study.network.thread;

import java.io.*;
import java.security.*;

public class ReturnDigest extends Thread {

  private final String filename;
  private byte[] digest;

  public ReturnDigest(String filename) {
	  System.out.println("ReturnDigest init: 1");
    this.filename = filename;
  }

  @Override
  public void run() {
    try {
	    System.out.println("ReturnDigest run start: 2");
      FileInputStream in = new FileInputStream(filename);
      MessageDigest sha = MessageDigest.getInstance("SHA-256");
      DigestInputStream din = new DigestInputStream(in, sha);
      while (din.read() != -1) ; // read entire file
      din.close();
      digest = sha.digest();
	    System.out.println("ReturnDigest run end: 3");
    } catch (IOException ex) {
      System.err.println(ex);
    } catch (NoSuchAlgorithmException ex) {
      System.err.println(ex);
    }
  }
  
  public byte[] getDigest() {
	  System.out.println("ReturnDigest getDigest: 4");
    return digest;
  }
}