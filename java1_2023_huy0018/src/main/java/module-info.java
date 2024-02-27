module com.example.projectgame_huy0018 {
    requires transitive javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens application to javafx.fxml;
    exports application;
}