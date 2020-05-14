package pl.lodz.p.ftims.model.user.Validators;

import pl.lodz.p.ftims.model.user.model.User;

public interface UserValidator {
    public boolean checkIfUserCorrect(User user);
}
