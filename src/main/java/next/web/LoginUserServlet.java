package next.web;

import core.db.DataBase;
import java.io.IOException;
import java.util.NoSuchElementException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import next.model.User;

@Slf4j
@WebServlet("/user/login")
public class LoginUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        DataBase.existUser(userId, password);
		User user = DataBase.findUserById(userId)
			.orElseThrow(() -> new NoSuchElementException("등록된 회원이 아닙니다"));

        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        resp.sendRedirect("/user/list");
    }
}
