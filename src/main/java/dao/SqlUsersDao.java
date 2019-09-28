package dao;

import org.sql2o.Sql2o;
import organisation.Departments;
import organisation.Users;

import java.util.List;

public class SqlUsersDao implements UsersDao {
    private final Sql2o sql2o;

    public SqlUsersDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Users users) {

    }

    @Override
    public void addUsersToDepartment(Users users, Departments department) {

    }

    @Override
    public List<Users> all() {
        return null;
    }

    @Override
    public List<Departments> allDepartmentsForUsers(int id) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void clearAll() {

    }

    @Override
    public Users findById(int usersId) {
        return null;
    }
}
