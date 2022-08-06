package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Response {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	public void makeResponse(String url, OutputStream out) {
		DataOutputStream dos = new DataOutputStream(out);

		byte[] body = null;

		if (url.equals("/index.html") || url.equals("/user/form.html")) {
			body = makeRequestBody(url);
			response200Header(dos, body.length);
		} else if (url.equals("/user/create")) {
			response302Header(dos, "/index.html");
		} else {
			body = "Hello World".getBytes();
		}

		responseBody(dos, body);
	}

	private byte[] makeRequestBody(String url) {
		byte[] body = null;

		try {
			body = Files.readAllBytes(new File("./webapp" + url).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return body;
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

	private void response302Header(DataOutputStream dos, String redirectionUrl) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Location: " + redirectionUrl + "\r\n");
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
