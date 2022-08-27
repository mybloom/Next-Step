package next.web;

import core.db.DataBase;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import next.model.User;

@WebServlet("/user/list")
public class ListUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		RequestDispatcher rd = null;

		HttpSession session = req.getSession();
		Object value = session.getAttribute("user");

		//TODO :  Optional 로 수정
		if (value == null) {
			resp.sendRedirect("/user/login.html");
			return;
		}

		req.setAttribute("users", DataBase.findAll());
		rd = req.getRequestDispatcher("/user/list.jsp");
		rd.forward(req, resp);
	}
}
