package dao;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import organisation.Departments;
import organisation.Users;

import java.util.List;

public class SqlDepartmentDao implements DepartmentDao {
private Sql2o sql2o;

    public SqlDepartmentDao(Sql2o sql2o){
        this.sql2o=sql2o;
        int id;

    }


    @Override
    public void add(Departments departments) {
String data="INSERT INTO departments(depDescription,depName,nbrEmployees)VALUES(:depDescription,:depName,:nbrEmployees);";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(data, true)
                    .bind(departments)
                    .executeUpdate()
                    .getKey();
            departments.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void addDepartmentToUsers(Departments departments, Users users) {

    }

    @Override
    public List<Departments> all() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM departments")
                    .executeAndFetch(Departments.class);
        }
    }


    @Override
    public List<Users> allUsersOfDepartment(int deptId) {
        return null;
    }

    @Override
    public Departments findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM departments WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Departments.class);
        }
    }

    @Override
    public void update(String depName, String depDescription, int nbrEmployees, int id) {
String data="UPDATE departments SET (depName,depDescription,nbrEmployees)=(:depName,:depDescription,:nbrEmployees)";//CHECK!!!
        try (Connection con=sql2o.open()){
            con.createQuery(data)
                    .addParameter("depName",depName)
                    .addParameter("depDescription",depDescription)
                    .addParameter("nbrEmployees",nbrEmployees)
                    .addParameter("id",id)
                    .executeUpdate();
        }catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {


    }

    @Override
    public void clearAll() {
        String data = "DELETE from restaurants";
        try (Connection con = sql2o.open()) {
            con.createQuery(data).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }



}
