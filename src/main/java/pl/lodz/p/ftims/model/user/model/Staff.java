package pl.lodz.p.ftims.model.user.model;


public class Staff extends User {
    public Staff(String id, String firstName, String secondName, String adress, int typeId, double salary, String login, String password) {
        super(id, firstName, secondName, adress, typeId, salary, login, password);
    }
}
