module compscichris.MariaDB.maven {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires com.sun.jna;

    exports Controller;
    exports View;
}