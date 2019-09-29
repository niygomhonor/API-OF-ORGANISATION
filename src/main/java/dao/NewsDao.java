package dao;

import organisation.News;

import java.util.List;

public interface NewsDao {
    void  add(News news);
    List<News>all();
    List<News> allNewsPostedByDepartment(int deptId);
//void update(int id,String contents,int deptId);
    void deleteById(int id);
    void clearAll();
}
