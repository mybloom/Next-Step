package util;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    /**
     * int read(char[] cbuf, int off, int len)      *
     * 인수로 들어간 cbuf의 문자열에서, off(=offset) 부터 len만큼의 문자열을 읽습니다.
     *
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
}
