package Servlets;

import Dao.UserDao;
import Dao.UserDaoJdbcTemplateImpl;
import Model.User;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@WebServlet("/user")
public class UserFirstServlet extends HttpServlet {
    private UserDao userDao;
    List<User> users;

    @Override
    public void init() throws ServletException {
        Properties properties = new Properties();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        try {
            properties.load(new FileInputStream(getServletContext().getRealPath("/WEB-INF/classes/db.properties")));
            String dbUrl = properties.getProperty("db.url");
            String dbUserName = properties.getProperty("db.username");
            String dbPassword = properties.getProperty("db.password");
            String driverClassName = properties.getProperty("db.DriverClassName");

            dataSource.setUsername(dbUserName);
            dataSource.setPassword(dbPassword);
            dataSource.setUrl(dbUrl);
            dataSource.setDriverClassName(driverClassName);

            userDao = new UserDaoJdbcTemplateImpl(dataSource);
        } catch (
                IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        HttpSession isSession = req.getSession(false);

        String role = "";
        String admin = "admin";

        if (userDao.isExists(login, password)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", login);
            session.setMaxInactiveInterval(30);
            String userColor = userDao.getColorUser(login);
            req.setAttribute("color", userColor);

            role = userDao.getRoleUser(login);
            if (role.equals(admin)) {
                users = userDao.findAll();
                req.setAttribute("UsersFromServer", users);
                req.setAttribute("color", userColor);
                req.getRequestDispatcher("/jsp/colorAdmin.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("/jsp/color.jsp").forward(req, resp);
            }
        } else if (isSession != null) {
            String userLogin = (String) isSession.getAttribute("user");
            String color = req.getParameter("color");
            userDao.update(userLogin, color);
            req.setAttribute("color", color);
            if (role.equals(admin)) {
                req.setAttribute("UsersFromServer", users);
                req.setAttribute("color", color);
                req.getRequestDispatcher("/jsp/colorAdmin.jsp").forward(req, resp);
            } else
                req.getRequestDispatcher("/jsp/color.jsp").forward(req, resp);
        } else if (login == null && isSession == null) {
            resp.sendRedirect("/ended");
        } else resp.sendRedirect("/user");
    }
}
