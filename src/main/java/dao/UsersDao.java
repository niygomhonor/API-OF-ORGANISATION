package dao;

import organisation.Departments;
import organisation.Users;

import java.util.List;

public interface UsersDao {
    void add(Users users);
    void addUsersToDepartment(Users users, Departments department);
    List<Users>all();
    List<Departments>allDepartmentsForUsers(int id);

    void deleteById(int id);
    void clearAll();

    Users findById(int usersId);
}
