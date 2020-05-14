package pl.lodz.p.ftims.model.user.model;

public abstract class User {
    public static final int WORKHOUSE_MANAGER = 0;
    public static final int STAFF = 1;
    public static final int STOREKEEPER = 2;
    protected String id;
    protected String firstName;
    protected String secondName;
    protected String address;
    protected int typeId;
    protected double salary;
    protected String login;
    protected String password;

    public User(String id, String firstName, String secondName, String address, int typeId, double salary, String login, String password) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.address = address;
        this.typeId = typeId;
        this.salary = salary;
        this.login = login;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
