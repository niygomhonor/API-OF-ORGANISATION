package organisation;

import java.util.Objects;

public class Users {
    private String name;
    private String userPosition;
    private  String role;
    private int id;
    public Users(String name,String userPosition,String role){

        this.name=name;
        this.role=role;
        this.userPosition=userPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users)) return false;
        Users users = (Users) o;
        return Objects.equals(name, users.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,userPosition,role,id);
    }
}
