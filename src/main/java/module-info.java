module com.example.graduationfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.graduationfx to javafx.fxml;
    exports com.example.graduationfx;
    exports com.example;
    opens com.example to javafx.fxml;
}