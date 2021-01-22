module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires kuromoji;
    requires sqlite.jdbc;
    requires java.sql;

    opens org.subtitle.assistor to javafx.fxml;
    exports org.subtitle.assistor;
}