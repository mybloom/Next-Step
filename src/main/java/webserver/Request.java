package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Getter;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.domain.HttpMethod;
import webserver.domain.RequestHeader;
import webserver.domain.RequestLine;
import webserver.domain.ResponseHeader;

@Getter
public class Request {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private RequestHeader requestHeader = new RequestHeader();
	private ResponseHeader responseHeader = new ResponseHeader();
	private int contentLength = 0;

	public ResponseHeader handleUserRequest(InputStream in) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

		parseHeader(bufferedReader);
		ResponseHeader responseHeader = processHttpMethod(bufferedReader);

		return responseHeader;
	}

	private void parseHeader(BufferedReader bufferedReader)
		throws IOException {

		//1.RequestLine
		String line = bufferedReader.readLine();
		log.debug("requestHeader: {}", line);
		requestHeader.makeRequestLine(line);

		//2.RequestHeaders
		List<String> requestHeaders = new ArrayList<>();
		// TODO : "".equals(line) 일 때까지 읽는 이유
		while (!"".equals(line)) {
			line = bufferedReader.readLine();
			log.debug("requestHeader: {}", line);

			if (line.contains("Content-Length")) {
				contentLength = getContentLength(line);
			}
			requestHeaders.add(line);
		}

		requestHeader.setRequestHeaders(requestHeaders);
	}

	private int getContentLength(String line) {
		String[] headerTokens = line.split(":");
		return Integer.parseInt(headerTokens[1].trim());
	}

	private ResponseHeader processHttpMethod(BufferedReader bufferedReader) throws IOException {
		HttpMethod httpMethod = requestHeader.getRequestLine().getHttpMethod();
		String url = requestHeader.getRequestLine().getUrl();

		if (httpMethod.equals(HttpMethod.GET)) {
			int indexOfQueryParameter = url.indexOf('?');
			if (indexOfQueryParameter != -1) {
				processQueryParameter(url, indexOfQueryParameter);
			}
		} else if (httpMethod.equals(HttpMethod.POST)) {
			String rawBody = IOUtils.readData(bufferedReader, contentLength);
			requestHeader.setBody(HttpRequestUtils.parseQueryString(rawBody));
			Map<String, String> body = requestHeader.getBody();

			if ("/user/create".equals(url)) {
				User user = new User(body.get("userId"), body.get("password"), body.get("name"),
					body.get("email"));

				DataBase.addUser(user);
				DataBase.findAll().stream()
					.forEach(userInDatabase -> log.debug("**Database.findAll() : {}",
						userInDatabase.toString()));
			} else if ("/user/login".equals(url)) {
				User user = DataBase.findUserById(body.get("userId"));
				if (user == null) {
					responseHeader.setResponseHeaders(
						List.of("Set-Cookie: logined=false" + "\r\n"));
					return responseHeader;
				}

				if (user.getPassword().equals(body.get("password"))) {
					responseHeader.setResponseHeaders(List.of("Set-Cookie: logined=true" + "\r\n"));
				} else {
					responseHeader.setResponseHeaders(
						List.of("Set-Cookie: logined=false" + "\r\n"));
				}
			}
		}

		return responseHeader;
	}

	private void processQueryParameter(String url, int indexOfQueryParameter) {
		String requestPath = url.substring(0, indexOfQueryParameter);
		String queryString = url.substring(indexOfQueryParameter + 1);

		Map<String, String> queryStringMap = HttpRequestUtils.parseQueryString(queryString);

		User user = new User();
		for (Entry<String, String> entry : queryStringMap.entrySet()) {
			switch (entry.getKey()) {
				case "userId":
					user.setUserId(entry.getValue());
					break;
				case "name":
					user.setName(entry.getValue());
					break;
				case "password":
					user.setPassword(entry.getValue());
					break;
				case "email":
					user.setEmail(URLDecoder.decode(entry.getValue(), StandardCharsets.UTF_8));
					break;
			}
		}

		DataBase.addUser(user);
		DataBase.findAll().stream()
			.forEach(userInDatabase -> log.debug("**Database.findAll() : {}",
				userInDatabase.toString()));
		//TODO : index.html로 redirection 되도록 처리해야하지 않을까? 요구사항에 없으니 패스
	}

}