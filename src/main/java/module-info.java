module com.system64.kurumisynth {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.system64.kurumisynth to javafx.fxml;
    exports com.system64.kurumisynth;
    opens com.system64.kurumisynth.view to javafx.fxml;
    exports com.system64.kurumisynth.view;
}