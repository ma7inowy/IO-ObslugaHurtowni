module pl.lodz.p.ftims {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires sqlite.jdbc;
    requires org.apache.commons.lang3;
    requires org.apache.pdfbox;
    opens pl.lodz.p.ftims to javafx.fxml;
    opens pl.lodz.p.ftims.model.client.model to javafx.base;
    exports pl.lodz.p.ftims;
}