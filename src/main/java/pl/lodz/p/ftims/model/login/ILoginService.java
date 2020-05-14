package pl.lodz.p.ftims.model.login;

public interface ILoginService {
    boolean login(String login, String password);

    boolean logout();
}
