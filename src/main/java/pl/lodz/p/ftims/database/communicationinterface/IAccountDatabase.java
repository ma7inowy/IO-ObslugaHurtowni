package pl.lodz.p.ftims.database.communicationinterface;

public interface IAccountDatabase {

    boolean addMoney(Double money); // dodaje

    boolean modifyMoney(Double money); // zmienia na podana kwote

    boolean subMoney(Double money); // odejmuje

    Double getMoney();
}
