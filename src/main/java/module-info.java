module remote.control.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.fazecast.jSerialComm;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires static lombok;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.controller;
}

