module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;
}