package dao;

import organisation.Departments;
import organisation.Users;

import java.util.List;

public interface DepartmentDao {
    void add(Departments departments);
    void addDepartmentToUsers(Departments departments, Users users);
    List<Departments> all();
    List<Users> allUsersOfDepartment(int deptId);
    List<Departments> allDepartmentNews(int newsId);
    Departments findById(int id);
void update( String depName,
         String depDescription,
         int nbrEmployees,
         int id);
    void deleteById(int id);
    void clearAll();
}
