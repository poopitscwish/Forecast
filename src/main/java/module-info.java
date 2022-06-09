module com.example.forecast {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.security.jgss;
    requires org.javamoney.moneta;
    requires org.javamoney.moneta.convert;
    requires org.javamoney.moneta.convert.ecb;
    requires org.javamoney.moneta.convert.imf;
    requires org.apache.commons.compress;
    requires poi.ooxml;
    requires poi.ooxml.schemas;
    requires poi;
    requires xmlbeans;
    requires org.apache.commons.collections4;
    requires java.desktop;
    opens com.example.forecast to javafx.fxml;
    exports com.example.forecast;
}