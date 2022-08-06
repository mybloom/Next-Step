package webserver.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseHeader {

	private ResponseStatusLine statusLine;
	private List<String> responseHeaders;
	private List<String> body;

}
