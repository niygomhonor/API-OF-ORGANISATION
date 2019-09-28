package organisation;

import java.util.Objects;

public class Departments {
    private String depName;
    private String depDescription;
    private int nbrEmployees;
    private int id;

    public Departments(String depDescription,String depName,int nbrEmployees){


    }
    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDepDescription() {
        return depDescription;
    }

    public void setDepDescription(String depDescription) {
        this.depDescription = depDescription;
    }

    public int getNbrEmployees() {
        return nbrEmployees;
    }

    public void setNbrEmployees(int nbrEmployees) {
        this.nbrEmployees = nbrEmployees;
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
        if (o == null || getClass() != o.getClass()) return false;
        Departments that = (Departments) o;
        return nbrEmployees == that.nbrEmployees &&
                id == that.id &&
                Objects.equals(depName, that.depName) &&
                Objects.equals(depDescription, that.depDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depName, depDescription, nbrEmployees, id);
    }
}
