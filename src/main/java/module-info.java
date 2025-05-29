module com.example.homework7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens com.homework7 to javafx.fxml;
    exports com.homework7;
}