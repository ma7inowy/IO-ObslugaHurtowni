package pl.lodz.p.ftims.model.client.model;

public enum ClientStateEnum {

    NEW_USER("new client"),
    CUSTOM("custom client"),
    VIP("regular client");

    private String value;

    ClientStateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}

