module com.example.batalhanaval {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.example.batalhanaval to javafx.fxml;
    exports com.example.batalhanaval;
}