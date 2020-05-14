package pl.lodz.p.ftims.model.client.model;

import java.util.List;

public class Client {
    private String id;
    private String name;
    private String state;
    private boolean isBanned;

    public Client( String name) {
        this.id = "-1";
        this.name = name;
        this.state = ClientStateEnum.NEW_USER.toString();
        this.isBanned = false;
    }

    public Client(String id, String name) {
        this.id = id;
        this.name = name;
        this.state = ClientStateEnum.NEW_USER.toString();
        this.isBanned = false;
    }

    public Client(String id, String name, String state, int isBanned) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.isBanned = isBanned == 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsBanned() {
        return isBanned;
    }

    public String getState() {
        return state;
    }

    public void setState(ClientStateEnum state) {
        this.state = state.toString();
    }

    public void setBanned(boolean banned) {
        this.isBanned = banned;
    }
}
