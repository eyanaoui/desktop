module com.example.user {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires java.mail;
    requires layout; // Check if these are correct module names and versions
    requires kernel;
    requires io;
    requires twilio;
    requires org.controlsfx.controls;
    requires jcaptcha.all; // Check if these are correct module names and versions
    requires javafx.web;
    requires org.apache.pdfbox;
    requires org.apache.poi.ooxml;

    exports utils;



    opens com.example.user to javafx.fxml;
    exports com.example.user;

}
