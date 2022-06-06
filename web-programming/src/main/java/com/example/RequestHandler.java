package com.example;

import com.example.db.DataBase;
import com.example.model.User;
import com.example.utils.HttpRequestUtils;
import com.example.utils.IOUtils;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Map;

/**
 * 사용자의 요청에 대한 처리와 응답에 대한 처리를 담당하는 가장 중심이 되는 클래스
 */
public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	public static final String ROOT_PATH = "/";
	public static final String WEBAPP_PATH = "web-programming/webapp";
	public static final String INDEX_PATH = "/index.html";
	
	private final Socket connection;
	
	// 사용자의 요청(Socket)을 인자 값으로 하여 run()을 수행
	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}
	
	@Override
	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}",
				connection.getInetAddress(), connection.getPort());
		
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// 사용자의 요청 정보에 대한 로깅
			BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.defaultCharset()));
			String line = br.readLine();
			String[] tokens = line.split("\\s");
			
			while (!"".equals(line)) {
				log.debug("header : {}", line);
				line = br.readLine();
			}
			
			String requestURL = extractRequestUrl(tokens);
			int index = requestURL.indexOf("?");
			
			String requestPath = "";
			if(index > 0) {
				requestPath = requestURL.substring(0, index);
			}
			
			// requestMapping
			if("/".equals(requestPath)) {
				String params = requestURL.substring(index + 1);
				Map<String, String> paramMap = HttpRequestUtils.parseQueryString(params);
	
				User user = new User(
						paramMap.get("userId")
						, paramMap.get("password")
						, paramMap.get("name")
						, paramMap.get("email")
				);
				
				log.debug("User : {}", user);
				DataBase.add(user);
				
				responseResource(out, INDEX_PATH);
			} else {
				responseResource(out, requestURL);
			}
			
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void responseResource(OutputStream out, String requestURL) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);
		File file = new File(WEBAPP_PATH + requestURL);
		byte[] body = Files.readAllBytes(file.toPath());
		response200Header(dos, body.length);
		responseBody(dos, body);
	}
	
	private int getContentLength(String line) {
		String[] headerTokens = line.split(":");
		return Integer.parseInt(headerTokens[1].trim());
	}
	
	// header의 첫 번째 라인은 필수로 3개의 토큰 정보를 갖는지?
	private String extractRequestUrl(String[] tokens) {
		String url = tokens[1];
		if (ROOT_PATH.equals(url)) {
			url = INDEX_PATH;
		}
		return url;
	}
	
	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
