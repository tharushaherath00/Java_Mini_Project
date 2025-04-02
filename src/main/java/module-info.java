module org.example.lms {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens org.example.lms to javafx.fxml;
    exports org.example.lms;
}