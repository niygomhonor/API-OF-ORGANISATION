package dao;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import organisation.News;

import java.util.List;


public class SqlNewsDao implements NewsDao {
    private final Sql2o sql2o;
    public SqlNewsDao(Sql2o sql2o){
        this.sql2o=sql2o;
    }

    @Override
    public void add(News news) {
String data="INSERT INTO news(contents,deptId) VALUES(:contents,:deptId)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(data, true)
                    .bind(news)
                    .executeUpdate()
                    .getKey();
            news.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<News> all() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM news")
                    .executeAndFetch(News.class);
        }
    }

    @Override
    public List<News> allNewsPostedByDepartment(int depId) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM reviews WHERE restaurantId = :restaurantId")
                    .addParameter("depId",depId )
                    .executeAndFetch(News.class);
        }
    }

    @Override
    public void deleteById(int id) {
        String data = "DELETE from news WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(data)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAll() {

    }
}
