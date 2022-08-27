package next.web;

import core.db.DataBase;
import java.io.IOException;
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
		String userId = req.getParameter("userId");

		if (!isTheLoginUser(req, resp, userId)) {
			return;
		}

		req.setAttribute("user", DataBase.findUserById(userId));
		RequestDispatcher rd = req.getRequestDispatcher("/user/update.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		String userId = req.getParameter("userId");
		String name = req.getParameter("name");
		String email = req.getParameter("email");

		if (!isTheLoginUser(req, resp, userId)) {
			return;
		}

		User user = DataBase.findUserById(userId);
		log.debug("**existing user data : {} ", user);
		user.update(name, email);
		log.debug("**modified user data : {} ", user);

		resp.sendRedirect("/user/list");
	}

	private boolean isTheLoginUser(HttpServletRequest req, HttpServletResponse resp,
		String userId) throws ServletException, IOException {

		HttpSession session = req.getSession();
		//todo: User로 캐스팅하기 전에 null 체크가 필요할 것 같다. 맞나?
		User userViaSession = (User) session.getAttribute("user");

		if (!userId.equals(userViaSession.getUserId())) {
			return false;
		}
		return true;
	}
}
