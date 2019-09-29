package organisation;

import java.util.Objects;

public class News {
    private String header;
    private String contents;
    private int id;
    private int deptId;

    public News(String header,String contents, int deptId) {
     this.header=header;
        this.contents = contents;
        this.deptId = deptId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeptId() {
        return deptId;
    }

    public String getHeader() {
        return header;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof News) )return false;
        News news = (News) o;
        return id == news.id &&
                deptId == news.deptId &&
                Objects.equals(contents, news.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents, id, deptId);
    }


}
