import dao.SqlDepartmentDao;
import dao.SqlNewsDao;
import dao.SqlUsersDao;
import org.sql2o.Sql2o;
import organisation.Departments;
import organisation.News;
import organisation.Users;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import com.google.gson.Gson;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import exceptions.ApiOexception;
import static spark.Spark.*;



public class App {
    public static void main(String[] args) {
        staticFileLocation("/public");
        SqlNewsDao newsDao;
        SqlDepartmentDao departmentDao;
        SqlUsersDao usersDao;
        Connection conne;
        Gson gson=new Gson();
        String connectionString = "jdbc:postgresql://localhost:5432/organisation_api";
        Sql2o sql2o = new Sql2o(connectionString, "wecode", "12345");

        usersDao=new SqlUsersDao(sql2o);
        departmentDao= new SqlDepartmentDao(sql2o);
        newsDao= new SqlNewsDao(sql2o);

        get("/welcome", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            return new ModelAndView(model, "welcome.hbs");
        }, new HandlebarsTemplateEngine());

        post("/departments/new", "application/json", (req, res) -> {
            Departments department = gson.fromJson(req.body(), Departments.class);
            departmentDao.add(department);
            res.status(201);
            return gson.toJson(department);
        });

        get("/departments", "application/json", (req, res) -> {
            return gson.toJson(departmentDao.all());
        });
        get("/departments/:id", "application/json", (req, res) -> { //accept a request in format JSON from an app
            int depId = Integer.parseInt(req.params("id"));
            return gson.toJson(departmentDao.findById(depId));
        });

        post("/departments/:deptId/news/new", "application/json", (req, res) -> {
            int deptId = Integer.parseInt(req.params("deptId"));
            News news = gson.fromJson(req.body(), News.class);
            news.setDeptId(deptId); //we need to set this separately because it comes from our route, not our JSON input.
            newsDao.add(news);
            res.status(201);
            return gson.toJson(news);
        });

        get("/departments/:id/news", "application/json", (req, res) -> {
            int departmentId = Integer.parseInt(req.params("id"));
            Departments departmentToFind = departmentDao.findById(departmentId);
            List<News> allNews;
            if (departmentToFind == null){
                throw new ApiOexception(404, String.format("No department with the id: \"%s\" exists", req.params("id")));
            }
            allNews = newsDao.allNewsPostedByDepartment(departmentId);
            return gson.toJson(allNews);
        });

        post("/users/new", "application/json", (req, res) -> {
            Users users = gson.fromJson(req.body(), Users.class);
            usersDao.add(users);
            res.status(201);
            return gson.toJson(users);
        });

        get("/users", "application/json", (req, res) -> {
            return gson.toJson(usersDao.all());
        });
    }

}
