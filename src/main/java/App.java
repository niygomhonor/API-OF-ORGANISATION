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
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
    public static void main(String[] args) {
        staticFileLocation("/public");
        SqlNewsDao newsDao;
        SqlDepartmentDao departmentDao;
        SqlUsersDao usersDao;

//        get("/welcome", (req, res) -> {
//            Map<String, Object> model = new HashMap<>();
//
//            return new ModelAndView(model, "welcome.hbs");
//        }, new HandlebarsTemplateEngine());

        Connection conn;
        Gson gson=new Gson();
        String connectionString = "jdbc:postgresql://ec2-174-129-18-42.compute-1.amazonaws.com:5432/dfoncevh9tiq6c";
        Sql2o sql2o = new Sql2o(connectionString, "vuvqxfrwxckaiy", "2633b82cc3626f3a44f91c06b112469ee6f7fa61c211547d81fdb80c531c30b0git");

        usersDao=new SqlUsersDao(sql2o);
        departmentDao= new SqlDepartmentDao(sql2o);
        newsDao= new SqlNewsDao(sql2o);
//
//        get("/welcome", (req, res) -> {
//            Map<String, Object> model = new HashMap<>();
//
//            return new ModelAndView(model, "welcome.hbs");
//        }, new HandlebarsTemplateEngine());

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
            int deptId = Integer.parseInt(req.params("id"));
            return gson.toJson(departmentDao.findById(deptId));
        });

        post("/departments/:id/news/new", "application/json", (req, res) -> {
            int deptId = Integer.parseInt(req.params("id"));
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



        post("/departments/:deptId/users/:userId", "application/json", (req, res) -> {
            int deptId = Integer.parseInt(req.params("deptId"));
            int userId = Integer.parseInt(req.params("userId"));
            Departments department = departmentDao.findById(deptId);
            Users users = usersDao.findById(userId);

            if (department != null && users != null){
                usersDao.addUsersToDepartment(users, department);
                res.status(201);
                return gson.toJson(String.format("User '%s' and Department '%s' have been associated",users.getName(), department.getDepName()));
            }
            else {
                throw new ApiOexception(404, String.format("Department or Users does not exist"));
            }
        });

        get("/departments/:id/users", "application/json", (req, res) -> {
            int deptId = Integer.parseInt(req.params("id"));
            List<Users> allUsers;
            Departments departmentToFind = departmentDao.findById(deptId);
            if (departmentToFind == null){
                throw new ApiOexception(404, String.format("No department with the id: \"%s\" exists", req.params("id")));
            }
            else if (departmentDao.allUsersOfDepartment(deptId).size()==0){
                return "{\"message\":\"No user belongs to this department.\"}";
            }

            else {

                return gson.toJson(departmentDao.allUsersOfDepartment(deptId));
            }
        });


//        //FILTERS
//        exception(ApiOexception.class, (exception, req, res) -> {
//            ApiOexception err = exception;
//            Map<String, Object> jsonMap = new HashMap<>();
//            jsonMap.put("status", err.getStatusCode());
//            jsonMap.put("errorMessage", err.getMessage());
//            res.type("application/json");
//            res.status(err.getStatusCode());
//            res.body(gson.toJson(jsonMap));
//        });
//
//        after((req, res) ->{
//            res.type("application/json");
//        });

//        ******************************************************************************

//      ALL ABOUT FRONT-END

////        ************************************************DEPARTMENT PART************************************************************
        get("/departments/new", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("allDepartments",departmentDao.all());
            return new ModelAndView(model, "department-form.hbs");
        },new HandlebarsTemplateEngine());

        post("/departments", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String depName = request.queryParams("depName");
            String depDescription = request.queryParams("depDescription");
            int nbrEmployees = Integer.parseInt(request.queryParams("nbrEmployees"));
            Departments newDepartment = new Departments(depName, depDescription, nbrEmployees);
            departmentDao.add(newDepartment);
            model.put("departments", departmentDao.all());
            model.put("depName",depName);
            model.put("depDescription",depDescription);
            model.put("nbrEmployees",nbrEmployees);
            return new ModelAndView(model, "dep-success.hbs");
        }, new HandlebarsTemplateEngine());

        get("/departments",(request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("departments", departmentDao.all());
            return new ModelAndView(model, "department.hbs");
        },new HandlebarsTemplateEngine());

//++++++++++++++++++++++++++++++++++++++++++++USER PART+++++++++++++++++++++++++++++++++++++++
        get("/user/new", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "user-form.hbs");
        },new HandlebarsTemplateEngine());

        post("/users", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String name = request.queryParams("name");
            String userPosition=request.queryParams("userPosition");
            String role = request.queryParams("role");
            Users newUser = new Users(name, userPosition, role);
            usersDao.add(newUser);
            model.put("users", usersDao.all());
            model.put("name",name);
            model.put("userPosition",userPosition);
            model.put("role",role);
            return new ModelAndView(model, "user-success.hbs");
        }, new HandlebarsTemplateEngine());

        get("/users", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();

            model.put("users", usersDao.all());
            return new ModelAndView(model, "user.hbs");
        },new HandlebarsTemplateEngine());

        //+++++++++++++++++++++++++++++++++NEWS PART+++++++++++++++++++++++++++++++++++++++++++++++++

        get("/news/new", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("allNews",newsDao.all());
            model.put("departments", departmentDao.all());
            return new ModelAndView(model, "news-form.hbs");
        },new HandlebarsTemplateEngine());

        post("/news", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String header=request.queryParams("header");
            String contents = request.queryParams("contents");
            int deptId = Integer.parseInt(request.queryParams("deptId"));
            News newNews = new News(header,contents, deptId);
            newsDao.add(newNews);
            model.put("news", newsDao.all());
            model.put("header",header);
            model.put("contents",contents);
            model.put("deptId",deptId);
            return new ModelAndView(model, "new-success.hbs");
        }, new HandlebarsTemplateEngine());

        get("/news", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();

            model.put("allNews",newsDao.all());
            return new ModelAndView(model, "news.hbs");
        },new HandlebarsTemplateEngine());

//++++++++++++++++++++++++++++++++++++++++++++++View news of specific department+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        post("/departments/:id/news", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();

            int deptId = Integer.parseInt(request.params("id"));
            Departments idToFind = departmentDao.findById(deptId);
            model.put("id", deptId);
            model.put("dataId", idToFind.getId());
            model.put("allDepartments",departmentDao.allDepartmentNews(deptId));

            return new ModelAndView(model, "department-news.hbs");
        },new HandlebarsTemplateEngine());

//        ++++++++add News to department++++++++++++++++++++++

        get("/departments/:id/news/new", (request, response) -> {
            Map<String, Object> user = new HashMap<>();
            int deptId = Integer.parseInt(request.params("id"));
            String header = request.queryParams("header");
            String content = request.queryParams("content");
            Departments newNews = new Departments(header, content, deptId);
            departmentDao.add(newNews);
            System.out.println(departmentDao.allDepartmentNews(deptId));
            return new ModelAndView(user, "new-success.hbs");
        }, new HandlebarsTemplateEngine());


        //*********************DEPARTMENT IN DETAILS**************************************************************************************

        get("/departments/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();

            int id = Integer.parseInt(request.params("id"));
            System.out.println(id);
            Departments findDepDet=departmentDao.findById(id);
            Departments idToFind = departmentDao.findById(Integer.parseInt(request.params("id")));

            model.put("id", findDepDet);

            model.put("departmentDetails",findDepDet);
            model.put("users", usersDao.all());
            return new ModelAndView(model, "dep-details.hbs");
        },new HandlebarsTemplateEngine());

//+++++++++++++++++++++USERS IN DETAILS++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        get("/users/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            int id = Integer.parseInt(request.params("id"));
            System.out.println(id);
            Users foundUserDet = usersDao.findById(id);
            Users user=usersDao.findById(Integer.parseInt(request.params("id")));
            model.put("id", foundUserDet);
            model.put("user",user);
            model.put("userDetails", foundUserDet);
            model.put("userDepartment", usersDao.allDepartmentsForUsers(id));
            model.put("allDepartments", departmentDao.all());
            return new ModelAndView(model, "user-details.hbs");
        },new HandlebarsTemplateEngine());

        get("/users/:id/edit", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            int idOfUserToEdit = Integer.parseInt(request.params("id"));
          Users editUser=usersDao.findById(idOfUserToEdit);
          model.put("editUser",editUser);
          model.put("editUser",true);
            return new ModelAndView(model, "user-form.hbs");
        },new HandlebarsTemplateEngine());

        get("/users/:id/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            int idOfUserToDelete = Integer.parseInt(request.params("id"));
            Users deleteUser=usersDao.deleteById(idOfUserToDelete);
            usersDao.deleteById(idOfUserToDelete);
            model.put("deleteUser",deleteUser);
            model.put("deleteUser",true);
            return new ModelAndView(model, "del-user.hbs");
        },new HandlebarsTemplateEngine());

        get("/departments/:id/users", (request, response) -> {
            Map<String, Object> user = new HashMap<>();
            int deptId = Integer.parseInt(request.params("id"));
            Departments idToFind = departmentDao.findById(deptId);
            user.put("depUsers", departmentDao.allUsersOfDepartment(deptId));
            user.put("deptId", deptId);
            user.put("deptId", idToFind.getId());
            user.put("allUsers", usersDao.all());
            return new ModelAndView(user, "department-users.hbs");
        }, new HandlebarsTemplateEngine());



        post("/departments/:deptId/users/new", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            int deptId = Integer.parseInt(req.params("deptId"));
            int userId = Integer.parseInt(req.params("userId"));
            Departments department = departmentDao.findById(deptId);
            Users users = usersDao.findById(userId);
            return new ModelAndView(model, "user-success.hbs");
        }, new HandlebarsTemplateEngine());

//        post("/departments/:deptId/users/:userId", (req, res) -> {
//
//            Map<String, Object> model = new HashMap<String, Object>();
//            int deptId = Integer.parseInt(req.params("deptId"));
//            int userId = Integer.parseInt(req.params("userId"));
//            Departments department = departmentDao.findById(deptId);
//            Users users = usersDao.findById(userId);
//
//            if (department != null && users != null){
//                usersDao.addUsersToDepartment(users, department);
//                res.status(201);
//                return ();
//            }
//            else {
//                throw new ApiOexception(404, String.format("Department or Users does not exist"));
//            }
//
//            return new ModelAndView(model, "user-success.hbs");
//        },new HandlebarsTemplateEngine());

        // ************************NEWS IN DETAILS**********************************************************************************

        get("/news/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            int deptId = Integer.parseInt(request.queryParams("deptId"));
            Departments idToFind = departmentDao.findById(deptId);
            model.put("deptId", idToFind);
            return new ModelAndView(model, "news-details.hbs");
        },new HandlebarsTemplateEngine());

        // *********************************HOME PATH*************************************************************************
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("allNews",newsDao.all());
            return new ModelAndView(model, "welcome.hbs");
        },new HandlebarsTemplateEngine());




        get("/staff", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("users", usersDao.all());
            return new ModelAndView(model, "Draft.hbs");
        },new HandlebarsTemplateEngine());

        get("/dept", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("departments", departmentDao.all());
            return new ModelAndView(model, "Draft.hbs");
        },new HandlebarsTemplateEngine());
//
    }
}



