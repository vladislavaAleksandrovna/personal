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
import java.util.Optional;
import java.util.Properties;

@WebServlet("/userId")
public class UserIdServlet extends HttpServlet {
    private UserDao userDao;
    List<User> users;


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
       // req.getRequestDispatcher("/jsp/colorAdmin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.valueOf(req.getParameter("id"));
        HttpSession httpSession = req.getSession(false);
        Optional<Integer> userId = Optional.empty();
        users = userDao.findAll();
        if (httpSession != null) {
            userId = userDao.findById(id);
            if (userId.isPresent()) {
                userDao.delete(id);
               req.getRequestDispatcher("/jsp/colorAdmin.jsp").forward(req, resp);
            } else {
                boolean errorId = true;
                req.setAttribute("errorId", errorId);
               req.setAttribute("idUser", userId);
               req.getRequestDispatcher("/jsp/colorAdmin.jsp").forward(req, resp);
            }
        }

    }
}
