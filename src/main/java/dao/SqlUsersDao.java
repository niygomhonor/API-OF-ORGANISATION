package dao;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import organisation.Departments;
import organisation.Users;

import java.util.ArrayList;
import java.util.List;

public class SqlUsersDao implements UsersDao {
    private final Sql2o sql2o;

    public SqlUsersDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Users users) {

        String data="INSERT INTO users (name, userPosition, role) VALUES (:name,:userPosition, :role)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(data, true)
                    .bind(users)
                    .executeUpdate()
                    .getKey();
            users.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void addUsersToDepartment(Users users, Departments departments) {

        String data="INSERT INTO users_in_department(departmentId,userId) VALUES (:deptId,:userId) ";
        try (Connection con = sql2o.open()) {
            con.createQuery(data)
                    .addParameter("deptId", departments.getId())
                    .addParameter("userId", users.getId())
                    .throwOnMappingFailure(false)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public List<Users> all() {

        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM users")
                    .executeAndFetch(Users.class);
        }
    }

    @Override
    public List<Departments> allDepartmentsForUsers(int userId) {
        List<Departments>departments=new ArrayList<>();
        String joinCode="SELECT departmentId FROM users_in_department WHERE userId=:userId";

        try (Connection con = sql2o.open()) {
            List<Integer> allDepartmentIds = con.createQuery(joinCode)
                    .addParameter("userId", userId)
                    .executeAndFetch(Integer.class);
            for (Integer deptId : allDepartmentIds){
                String departmentQuery = "SELECT * FROM departments WHERE id = :deptId";
                departments.add(
                        con.createQuery(departmentQuery)
                                .addParameter("deptId", deptId)
                                .executeAndFetchFirst(Departments.class));
            }
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
        return departments;
    }


    @Override
    public void deleteById(int id) {
        String sql = "DELETE from users WHERE id = :id";
        String deleteCode = "DELETE from users_in_department WHERE userId = :userId";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();

            con.createQuery(deleteCode)
                    .addParameter("userId", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAll() {

        String data = "DELETE FROM users";
        try (Connection con = sql2o.open()) {
            con.createQuery(data).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Users findById(int id) {
        try(Connection con = sql2o.open()){

            return con.createQuery("SELECT * FROM users WHERE id = :userId")
                    .addParameter("userId", id)
                    .executeAndFetchFirst(Users.class);
        }
    }
}
