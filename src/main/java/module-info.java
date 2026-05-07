module compscichris.MariaDB.maven {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires com.sun.jna;
    requires jdk.compiler;

    exports Controller;
    exports View;
}