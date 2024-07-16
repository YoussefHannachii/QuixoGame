module com.example.quixogameproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    exports com.example.quixogameproject;
    opens com.example.quixogameproject to javafx.fxml;

    exports com.example.quixogameproject.controllers to javafx.fxml;
    opens com.example.quixogameproject.controllers to javafx.fxml;
}