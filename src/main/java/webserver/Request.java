package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.domain.HttpMethod;
import webserver.domain.RequestHeader;
import webserver.domain.RequestLine;

public class Request {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private RequestHeader requestHeader;

	public RequestLine handleUserRequest(InputStream in) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

		RequestLine requestLine = parseHeader(bufferedReader);
		processHttpMethod(requestLine);

		return requestLine;
	}

	private RequestLine parseHeader(BufferedReader bufferedReader)
		throws IOException {
		RequestHeader requestHeader = new RequestHeader();

		//1.RequestLine
		String line = bufferedReader.readLine();
		log.debug("requestHeader: {}", line);
		requestHeader.makeRequestLine(line);

		//2.RequestHeaders
		List<String> requestHeaders = new ArrayList<>();
		int contentLength = 0;
		// TODO : "".equals(line) 일 때까지 읽는 이유
		while (!"".equals(line)) {
			line = bufferedReader.readLine();
			log.debug("requestHeader: {}", line);

			if (line.equals("Content-Length")) {
				contentLength = getContentLength(line);
			}
			requestHeaders.add(line);

			//TODO : 이거 필요없을 것 같은데 , 과거의 내가 왜 해논걸까?
			if (line == null) {
				return requestHeader.getRequestLine();
			}
		}
		requestHeader.setRequestHeaders(requestHeaders);

		return requestHeader.getRequestLine();
	}

	private int getContentLength(String line) {
		String[] headerTokens = line.split(":");
		return Integer.parseInt(headerTokens[1].trim());
	}

	private void processHttpMethod(RequestLine requestLine) {
		HttpMethod httpMethod = requestLine.getHttpMethod();
		String url = requestLine.getUrl();

		if (httpMethod.equals(HttpMethod.GET)) {
			int indexOfQueryParameter = url.indexOf('?');
			if (indexOfQueryParameter != -1) {
				processQueryParameter(url, indexOfQueryParameter);
			}
		} else if (httpMethod.equals(HttpMethod.POST)) {

		}

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