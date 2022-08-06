package webserver.domain;

import lombok.Getter;

@Getter
public class ResponseStatusLine {

	private String httpVersion;
	private HttpStatus httpStatus;

}
