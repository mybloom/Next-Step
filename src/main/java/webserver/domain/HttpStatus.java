package webserver.domain;

public enum HttpStatus {

	OK(200,"OK"),
	FOUND(302,"Found");

	private int code;
	private String message;

	HttpStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
