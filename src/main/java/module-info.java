module com.example.examen2interfaces {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires javafx.swing;


    opens com.example.examen2interfaces to javafx.fxml;
    exports com.example.examen2interfaces;
}