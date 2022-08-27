package next.web;

import core.db.DataBase;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/user/update")
public class UpdateUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(CreateUserServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {

		isLoginUser(req, resp);

		req.setAttribute("user", DataBase.findUserById(req.getParameter("userId")).get());
		RequestDispatcher rd = req.getRequestDispatcher("/user/update.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		String userId = req.getParameter("userId");
		String name = req.getParameter("name");
		String email = req.getParameter("email");

		isLoginUser(req, resp);

		User user = DataBase.findUserById(userId)
			.orElseThrow(() -> new NoSuchElementException("해당 사용자 정보가 없습니다."));
		log.debug("**existing user data : {} ", user);
		user.update(name, email);
		log.debug("**modified user data : {} ", user);

		resp.sendRedirect("/user/list");
	}

	private void isLoginUser(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		String userId = req.getParameter("userId");

		HttpSession session = req.getSession();
		//todo: User로 캐스팅하기 전에 null 체크가 필요할 것 같다. 맞나? null을 cast해도 되긴 하는듯. 뭘 하는거지?
		//(User) session.getAttribute("user");
		User userViaSession = Optional.ofNullable((User) session.getAttribute("user"))
			.orElseThrow(() -> new SecurityException("로그인한 사용자만 접근할 수 있습니다."));

		//todo: userViaSession이 null이면 if 조건절이 에러나고 그 다음 줄 실행이 안된다.
		if (!userId.equals(userViaSession.getUserId())) {
			throw new SecurityException("본인의 계정정보만 접근할 수 있습니다.");
		}
	}
}
