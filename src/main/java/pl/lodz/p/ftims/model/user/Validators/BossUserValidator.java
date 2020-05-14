package pl.lodz.p.ftims.model.user.Validators;

import pl.lodz.p.ftims.model.user.model.User;

public class BossUserValidator implements UserValidator {
    @Override
    public boolean checkIfUserCorrect(User user) {
        return user.getPassword() != null
                && user.getLogin() != null
                && user.getAddress() != null
                && user.getFirstName() != null
                && user.getId() != null
                && user.getSalary() != 0.0
                && user.getSecondName() != null
                && user.getTypeId() != 0;
    }
}
