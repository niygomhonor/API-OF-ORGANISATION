package dao;

import organisation.News;

import java.util.List;

public interface NewsDao {
    void  add(News news);
    List<News>all();
    List<News> allNewsPostedByDepartment(int depId);

    void deleteById(int id);
    void clearAll();
}
