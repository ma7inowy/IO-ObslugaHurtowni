package pl.lodz.p.ftims.database;

import javafx.scene.chart.PieChart;

public class DatabaseSingleton {
    private static final Database DATABASE = new Database();

    public static Database getInstance() {
        return DATABASE;
    }
}
